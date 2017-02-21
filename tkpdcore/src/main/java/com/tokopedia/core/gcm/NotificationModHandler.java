package com.tokopedia.core.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;

public class NotificationModHandler {

    private Context mContext;
    
    
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
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    	notificationManager.cancel(100);
    	LocalCacheHandler.clearCache(mContext, TkpdCache.GCM_NOTIFICATION);
    }

}
