package com.tokopedia.tkpd.fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.notification.applink.ApplinkTypeFactory;
import com.tokopedia.core.gcm.notification.applink.ApplinkTypeFactoryList;
import com.tokopedia.core.gcm.notification.applink.ApplinkVisitor;
import com.tokopedia.tkpd.deeplink.DeepLinkReceiver;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;

/**
 * Created by alvarisi on 3/5/17.
 */

public class ApplinkResetReceiver extends BroadcastReceiver {
    private static final String TAG = DeepLinkReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String category = intent.getStringExtra(Constants.EXTRA_APPLINK_CATEGORY);
        switch (category){
            case Constants.ARG_NOTIFICATION_APPLINK_MESSAGE:
                NotificationModHandler.cancelNotif(context, Constants.ARG_NOTIFICATION_APPLINK_MESSAGE_ID);
                break;
        }

    }
}
