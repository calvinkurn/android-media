package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.R;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/19/17.
 */

public class SellingInvalidResiNotification extends BaseNotification {
    public SellingInvalidResiNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        Intent intent = RouteManager.getIntent(mContext, ApplinkConst.SELLER_PURCHASE_AWB_INVALID, "");
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(intent);
        mNotificationPass.classParentStack = ((TkpdCoreRouter) mContext.getApplicationContext()).getHomeClass();
        mNotificationPass.title = mContext.getString(R.string.title_transaction_update_resi);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        Bundle bundle = intent.getExtras();
        mNotificationPass.extraData = bundle;
        mNotificationPass.mIntent.putExtras(bundle);
    }
}
