package com.tokopedia.sellerapp.fcm.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.sellerapp.R;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;

/**
 * Created by alvarisi on 3/7/17.
 */

public class TopAdsTopupSuccessNotification extends BaseNotification {
    public static final int NOTIFICATION_ID = 1101;
    public static final String NOTIFICATION_LABEL = "Top Ads Topup Success";

    public TopAdsTopupSuccessNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        Intent intent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, TopAdsDashboardActivity.class)
        );
        intent.putExtra(UnifyTracking.EXTRA_LABEL, NOTIFICATION_LABEL);

        mNotificationPass.mIntent = intent;

        mNotificationPass.classParentStack = TopAdsDashboardActivity.class;
        mNotificationPass.title = data.getString(ARG_NOTIFICATION_TITLE, mContext.getString(R.string.title_push_notif_general));
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        UnifyTracking.eventPushNotifSuccessTopadsReceived(mContext);
    }

    @Override
    protected void buildDefaultConfiguration() {
        super.buildDefaultConfiguration();
        configuration.setNotificationId(NOTIFICATION_ID);
    }
}
