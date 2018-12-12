package com.tokopedia.core.gcm.notification.promotions;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.RouterUtils;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_IMAGE;

/**
 * Created by alvarisi on 1/16/17.
 */

public abstract class BasePromoNotification extends BaseNotification {
    protected INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;
    protected SessionHandler sessionHandler;
    protected BasePromoNotification(Context context) {
        super(context);
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
        sessionHandler = RouterUtils.getRouterFromContext(context).legacySessionHandler();
    }

    @Override
    public void proccessReceivedNotification(Bundle incomingMessage) {
        buildDefaultConfiguration();
        Bundle data = incomingMessage;
        if (TextUtils.isEmpty(incomingMessage.getString(ARG_NOTIFICATION_IMAGE))) {
            data.putString("img_uri", incomingMessage.getString(ARG_NOTIFICATION_IMAGE, ""));
            data.putString("img_uri_600", incomingMessage.getString(ARG_NOTIFICATION_IMAGE, ""));
            configuration.setNetworkIcon(true);
        }
        data = mNotificationAnalyticsReceiver.buildAnalyticNotificationData(data);
        mNotificationPass.extraData = data;
        configureNotificationData(data);
        showNotification(data);
    }
}
