package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.util.AppWidgetUtil;

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
                new Intent(mContext, TkpdCoreRouter.getSellingActivityClass(mContext))
        );
        mNotificationPass.classParentStack = TkpdCoreRouter.getSellingActivityClass(mContext);
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
