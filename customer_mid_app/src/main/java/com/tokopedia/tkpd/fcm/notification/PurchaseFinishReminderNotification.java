package com.tokopedia.tkpd.fcm.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalOrder;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.customer_mid_app.R;

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
        Intent orderListIntent = RouteManager.getIntent(mContext, ApplinkConst.PURCHASE_ORDER);
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(orderListIntent);
        mNotificationPass.classParentStack = ((TkpdCoreRouter) mContext.getApplicationContext()).getHomeClass();
        mNotificationPass.title = mContext.getString(R.string.purchase_confirm_receiving);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);

        Bundle bundle = new Bundle();
        bundle.putString(ApplinkConstInternalOrder.KEY_LABEL, ApplinkConstInternalOrder.MARKETPLACE);
        bundle.putString(ApplinkConstInternalOrder.ORDER_FILTER_ID, ApplinkConstInternalOrder.MENUNGGU_KONFIRMASI);

        mNotificationPass.extraData = bundle;
        mNotificationPass.mIntent.putExtras(bundle);
    }
}
