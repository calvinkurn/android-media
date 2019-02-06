package com.tokopedia.core.gcm.notification.promotions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.utils.NotificationUtils;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_URL;

/**
 * Created by alvarisi on 1/16/17.
 */

public class DeeplinkNotification extends BasePromoNotification {
    public DeeplinkNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configurePromoIntent(
                new Intent(mContext, TkpdCoreRouter.getDeeplinkClass(mContext)),
                data
        );
        mNotificationPass.classParentStack = TkpdCoreRouter.getDeeplinkClass(mContext);
        mNotificationPass.title = data.getString(ARG_NOTIFICATION_TITLE, "");
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.isAllowedBigStyle = true;
        if(data.getString(ARG_NOTIFICATION_URL) != null) {
            mNotificationPass.mIntent.setData(Uri.parse(data.getString(ARG_NOTIFICATION_URL)));
        }else {
            mNotificationPass.mIntent.setData(null);
        }
        mNotificationPass.mIntent.putExtras(data);
    }
}
