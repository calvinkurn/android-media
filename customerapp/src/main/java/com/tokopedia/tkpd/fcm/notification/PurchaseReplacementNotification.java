package com.tokopedia.tkpd.fcm.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.tkpd.R;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

public class PurchaseReplacementNotification extends BaseNotification {
    public PurchaseReplacementNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, TransactionPurchaseRouter.getPurchaseActivityClass())
        );
        mNotificationPass.classParentStack = TransactionPurchaseRouter.getPurchaseActivityClass();
        mNotificationPass.title = mContext.getString(R.string.purchase_replacement);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        Bundle bundle = new Bundle();
        bundle.putString(TransactionPurchaseRouter.EXTRA_STATE_MARKETPLACE_FILTER, TransactionPurchaseRouter.PURCHASE_CANCEL_FILTER_ID);
        mNotificationPass.extraData = bundle;
        mNotificationPass.mIntent.putExtras(bundle);
    }
}
