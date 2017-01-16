package com.tokopedia.core.gcm.model.promotions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.CustomerRouter;
import com.tokopedia.core.router.home.HomeRouter;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_URL;

/**
 * Created by alvarisi on 1/16/17.
 */

public class DeeplinkNotification extends BasePromoNotification {
    protected DeeplinkNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configurePromoIntent(
                new Intent(mContext, CustomerRouter.getDeeplinkClass()),
                data
        );
        mNotificationPass.classParentStack = CustomerRouter.getDeeplinkClass();
        mNotificationPass.title = data.getString(ARG_NOTIFICATION_TITLE, "");
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.isAllowedBigStyle = true;
        mNotificationPass.mIntent.setData(Uri.parse(data.getString(ARG_NOTIFICATION_URL)));
        mNotificationPass.mIntent.putExtras(data);
    }
}
