package com.tokopedia.core.gcm.notification.promotions;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.utils.NotificationUtils;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;

/**
 * Created by alvarisi on 1/16/17.
 */

public class GeneralNotification extends BasePromoNotification {
    public GeneralNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configurePromoIntent(
                ((TkpdCoreRouter) mContext.getApplicationContext()).getHomeIntent(mContext),
                data
        );
        mNotificationPass.classParentStack = ((TkpdCoreRouter) mContext.getApplicationContext()).getHomeClass();
        mNotificationPass.title = data.getString(ARG_NOTIFICATION_TITLE, "");
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.isAllowedBigStyle = true;

        mNotificationPass.mIntent.putExtras(data);
    }
}
