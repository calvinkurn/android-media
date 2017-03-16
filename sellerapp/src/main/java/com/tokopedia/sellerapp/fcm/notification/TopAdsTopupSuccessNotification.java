package com.tokopedia.sellerapp.fcm.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.seller.topads.view.activity.TopAdsDashboardActivity;
import com.tokopedia.sellerapp.R;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 3/7/17.
 */

public class TopAdsTopupSuccessNotification extends BaseNotification {
    protected TopAdsTopupSuccessNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, TopAdsDashboardActivity.class)
        );
        mNotificationPass.classParentStack = TopAdsDashboardActivity.class;
        mNotificationPass.title = mContext.getString(R.string.title_push_notif_general);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
    }
}
