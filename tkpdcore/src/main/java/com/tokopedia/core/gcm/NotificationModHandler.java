package com.tokopedia.core.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;

import java.util.ArrayList;

public class NotificationModHandler {
	
	private NotificationManager mNotificationManager;
	private ArrayList<String> Content = new ArrayList<String>();
    private ArrayList<String> Desc = new ArrayList<String>();
    private ArrayList<Integer> Code = new ArrayList<Integer>();
    private int NotificationCode;
    private Context context;
    
    
    public NotificationModHandler(Context context, int code) {
    	NotificationCode = code;
    	mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	this.context = context;
    }

    public NotificationModHandler(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.context = context;
    }

    public static void clearCacheIfFromNotification(Context context, Intent intent) {
        if (intent != null && intent.hasExtra(Constants.EXTRA_FROM_PUSH)){
            if (intent.getBooleanExtra(Constants.EXTRA_FROM_PUSH, false)){
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(100);
                LocalCacheHandler.clearCache(context, TkpdCache.GCM_NOTIFICATION);
            }
        }
    }

    public void cancelNotif() {
    	mNotificationManager.cancel(100);
    	LocalCacheHandler.clearCache(context, TkpdCache.GCM_NOTIFICATION);
    }

}
