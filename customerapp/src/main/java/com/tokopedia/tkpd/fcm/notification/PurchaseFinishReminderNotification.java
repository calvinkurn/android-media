package com.tokopedia.tkpd.fcm.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.buyerorder.list.common.OrderListContants;
import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.buyerorder.list.data.OrderMarketplaceFilterId;
import com.tokopedia.buyerorder.list.view.activity.OrderListActivity;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.intl.R;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/19/17.
 */

public class PurchaseFinishReminderNotification extends BaseNotification {
    public PurchaseFinishReminderNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, OrderListActivity.class)
        );
        mNotificationPass.classParentStack = OrderListActivity.class;
        mNotificationPass.title = mContext.getString(R.string.purchase_confirm_receiving);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);

        Bundle bundle = new Bundle();
        bundle.putString(OrderCategory.KEY_LABEL, OrderCategory.MARKETPLACE);
        bundle.putString(OrderListContants.ORDER_FILTER_ID, OrderMarketplaceFilterId.MENUNGGU_KONFIRMASI);

        mNotificationPass.extraData = bundle;
        mNotificationPass.mIntent.putExtras(bundle);
    }
}
