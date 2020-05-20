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
import com.tokopedia.core.util.AppWidgetUtil;

import okhttp3.Route;

/**
 * Created by alvarisi on 1/16/17.
 */

public class NewOrderNotification extends BaseNotification {
    public NewOrderNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle incomingMessage) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                RouteManager.getIntent(mContext, ApplinkConst.SELLER_NEW_ORDER, "")
        );
        mNotificationPass.classParentStack = ((TkpdCoreRouter) mContext.getApplicationContext()).getHomeClass();
        mNotificationPass.title = String.format(
                "%s %s",
                incomingMessage.getString("counter"),
                mContext.getString(R.string.title_new_order)
        );
        mNotificationPass.ticker = mContext.getString(R.string.msg_new_order);
        mNotificationPass.description = mContext.getString(R.string.msg_new_order);
        AppWidgetUtil.sendBroadcastToAppWidget(mContext);
    }
}
