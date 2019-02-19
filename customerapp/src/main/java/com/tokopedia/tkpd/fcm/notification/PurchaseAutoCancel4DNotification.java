package com.tokopedia.tkpd.fcm.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.tkpd.R;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/19/17.
 */

public class PurchaseAutoCancel4DNotification extends BaseNotification {
    public PurchaseAutoCancel4DNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, TransactionPurchaseRouter.getPurchaseActivityClass())
        );
        mNotificationPass.classParentStack = TransactionPurchaseRouter.getPurchaseActivityClass();
        mNotificationPass.title = mContext.getString(R.string.purchase_canceled);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        Bundle bundle = new Bundle();
        bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                TransactionPurchaseRouter.TAB_POSITION_PURCHASE_ALL_ORDER);
        bundle.putString(TransactionPurchaseRouter.EXTRA_STATE_MARKETPLACE_FILTER, TransactionPurchaseRouter.PURCHASE_AUTO_CANCEL_FILTER_ID);
        mNotificationPass.extraData = bundle;
        mNotificationPass.mIntent.putExtras(bundle);
    }
}
