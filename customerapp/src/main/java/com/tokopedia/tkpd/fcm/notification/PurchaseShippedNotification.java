package com.tokopedia.tkpd.fcm.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.tkpd.R;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.view.activity.OrderListActivity;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/19/17.
 */

public class PurchaseShippedNotification extends BaseNotification {
    public PurchaseShippedNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, OrderListActivity.class)
        );
        mNotificationPass.classParentStack = OrderListActivity.class;
        mNotificationPass.title = mContext.getString(R.string.purchase_shipped);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);

        Bundle bundle = new Bundle();
        bundle.putString(OrderCategory.KEY_LABEL, OrderCategory.MARKETPLACE);

        mNotificationPass.extraData = bundle;
        mNotificationPass.mIntent.putExtras(bundle);
    }
}
