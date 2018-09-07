package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.SellerRouter;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/19/17.
 */

public class SellingOrderDeliveredNotification extends BaseNotification {
    public SellingOrderDeliveredNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        Intent intent = SellerRouter.getActivitySellingTransactionShippingStatus(mContext);
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(intent);
        mNotificationPass.classParentStack = SellerRouter.getSellingActivityClass();
        mNotificationPass.title = mContext.getString(R.string.title_transaction_order_delivered);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        Bundle bundle = intent.getExtras();
        mNotificationPass.extraData = bundle;
        mNotificationPass.mIntent.putExtras(bundle);
    }
}