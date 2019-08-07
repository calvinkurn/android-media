package com.tokopedia.tkpd.fcm.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core2.R;
import com.tokopedia.transaction.orders.orderlist.common.OrderListContants;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.data.OrderMarketplaceFilterId;
import com.tokopedia.transaction.orders.orderlist.view.activity.OrderListActivity;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/16/17.
 */

public class PurchaseDeliveredNotification extends BaseNotification {
    public PurchaseDeliveredNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, OrderListActivity.class)
        );
        mNotificationPass.classParentStack = OrderListActivity.class;
        mNotificationPass.title = mContext.getString(R.string.title_notif_purchase_delivered);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);

        Bundle bundle = new Bundle();
        bundle.putString(OrderCategory.KEY_LABEL, OrderCategory.MARKETPLACE);
        bundle.putString(OrderListContants.ORDER_FILTER_ID, OrderMarketplaceFilterId.PESANAN_TIBA);

        mNotificationPass.extraData = bundle;
        mNotificationPass.mIntent.putExtras(bundle);
    }
}
