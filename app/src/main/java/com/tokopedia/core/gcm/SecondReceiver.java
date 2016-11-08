package com.tokopedia.core.gcm;

import com.tokopedia.core.home.ParentIndexHome;
import com.tokopedia.core.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SecondReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		buildNotification(context);
		
	}
	
	@SuppressLint("NewApi")
	private void buildNotification(Context context){
			int currentVersion = android.os.Build.VERSION.SDK_INT;
			int requireVersion = android.os.Build.VERSION_CODES.JELLY_BEAN;
		  NotificationManager notificationManager 
		  = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		  Notification.Builder builder = new Notification.Builder(context);
		  
		  Intent intent = new Intent(context, ParentIndexHome.class);
		  PendingIntent pendingIntent  = PendingIntent.getActivity(context, 0, intent, 0);
		  
		  builder
		  .setSmallIcon(R.drawable.qc_launcher)
		  .setContentTitle("ContentTitle")
		  .setContentText("ContentText")
		  .setContentInfo("ContentInfo")
		  .setTicker("Ticker")
		  .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)
		  .setContentIntent(pendingIntent)
		  .setAutoCancel(true);
		  
		  if (currentVersion >= requireVersion ){
			  Notification notification = builder.build();
			  notificationManager.notify(R.drawable.qc_launcher, notification);
		  }else{
			  Notification notification = builder.getNotification();
			  notificationManager.notify(R.drawable.qc_launcher, notification);
		  }  
	}
	
}
