package progressindicatorapp.app;

import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void startProgressBar(View view) {
    	if(view instanceof Button) {

    		final NotificationManager mNotifyManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    		
    		// Use NotificationCompat so we can use the same notification on APIv4+
    		// Requires android-support-v4.jar (in Eclipse: right click on project, Android Tools->Add Support Library...)
    		final Builder mBuilder = new NotificationCompat.Builder(this);
    		mBuilder.setContentTitle("Downloading the news")
    				.setContentText("Starting...")
    				.setSmallIcon(R.drawable.ic_launcher);
    		
    		// Internal notification ID for this notification
    		final int ID = 0;
    		// Activity to start when notification is clicked
			final PendingIntent clickIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
    		
    		new Thread(
    			new Runnable() {
					@Override
					public void run() {
						
						final int max = 100;
						final int step = 5;
						
						// Update the notification with download status
						for(int progress=0;progress<max;progress+=step) {
							mBuilder.setProgress(max, progress, false)
									// Urgh
									.setContentText(((int)((double)progress/max*100))+"% downloaded");
							Notification notification = mBuilder.build();
							// Click notification to load the app (or not right now)
							// This is required, otherwise 1.6 devices crash with contentIntent required
							notification.contentIntent = clickIntent;
							mNotifyManager.notify(ID, notification);
							try {
								// Simulate something happening
								Thread.sleep(100);
							} catch (InterruptedException e) {
							}
						}
						
						// Set the notification to complete
						mBuilder.setContentText("100% complete")
								.setProgress(0, 0, false);
						Notification notification = mBuilder.build();
						// Click notification to load the app (or not right now)
						// This is required, otherwise 1.6 devices crash with contentIntent required
						notification.contentIntent = clickIntent;
						mNotifyManager.notify(ID, notification);
					}
    			}
    		).start();
    		
    	}
    }
    
}
