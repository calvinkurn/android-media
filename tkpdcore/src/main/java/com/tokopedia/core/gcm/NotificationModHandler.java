package com.tokopedia.core.gcm;

import android.app.NotificationManager;
import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;

public class NotificationModHandler {

    private Context mContext;
    
    public NotificationModHandler(Context context) {
    	this.mContext = context;
    }

    /**
     * Used to clear opened notification
     */
    public void cancelNotif() {
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    	notificationManager.cancel(100);
    	LocalCacheHandler.clearCache(mContext, TkpdCache.GCM_NOTIFICATION);
    }

}
