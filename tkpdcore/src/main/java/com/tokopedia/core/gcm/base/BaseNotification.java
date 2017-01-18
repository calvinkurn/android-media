package com.tokopedia.core.gcm.base;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.gcm.BuildAndShowNotification;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.Visitable;
import com.tokopedia.core.gcm.model.NotificationPass;

/**
 * @author  by alvarisi on 1/12/17.
 */

public abstract class BaseNotification implements Visitable {
    protected final Context mContext;
    private FCMCacheManager mFCMCacheManager;
    protected NotificationPass mNotificationPass;
    protected BuildAndShowNotification mBuildAndShowNotification;

    protected BaseNotification(Context context) {
        mContext = context;
        mNotificationPass = new NotificationPass();
        mBuildAndShowNotification = new BuildAndShowNotification(mContext);
        mFCMCacheManager = new FCMCacheManager(mContext);
    }

    @Override
    public void proccessReceivedNotification(Bundle incomingMessage) {
        configureNotificationData(incomingMessage);
        showNotification(incomingMessage);
    }

    public NotificationPass getNotificationPassData(){
        if(mNotificationPass != null){
            return mNotificationPass;
        }
        throw new IllegalArgumentException("Notification pass not allowed to null");
    }

    protected void showNotification(Bundle incomingMessage){
        mBuildAndShowNotification.buildAndShowNotification(mNotificationPass, incomingMessage);
    }

    protected abstract void configureNotificationData(Bundle data);
}
