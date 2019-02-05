package com.tokopedia.tkpd.fcm.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core2.R;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/16/17.
 */

public class PurchaseAcceptedNotification extends BaseNotification {
    public PurchaseAcceptedNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, TransactionPurchaseRouter.getPurchaseActivityClass())
        );
        mNotificationPass.classParentStack = TransactionPurchaseRouter.getPurchaseActivityClass();
        mNotificationPass.title = mContext.getString(R.string.title_notif_purchase_accepted);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        Bundle bundle = new Bundle();
        bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER);
        mNotificationPass.extraData = bundle;
        mNotificationPass.mIntent.putExtras(bundle);
    }
}
