package com.tokopedia.core.gcm.notification.promotions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.utils.NotificationUtils;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;

/**
 * Created by alvarisi on 1/16/17.
 */

public class WishlistNotification extends BasePromoNotification {
    public WishlistNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configurePromoIntent(
                new Intent(mContext, TkpdCoreRouter.getSimpleHomeActivityClass()),
                data
        );
        mNotificationPass.classParentStack = TkpdCoreRouter.getSimpleHomeActivityClass();
        mNotificationPass.title = data.getString(ARG_NOTIFICATION_TITLE, "");
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.isAllowedBigStyle = true;
        data.putInt(TkpdCoreRouter.FRAGMENT_TYPE, TkpdCoreRouter.WISHLIST_FRAGMENT);
        mNotificationPass.mIntent.putExtras(data);
    }

    @Override
    protected void showNotification(Bundle inComingBundle) {
        if (sessionHandler.isV4Login()) {
            super.showNotification(inComingBundle);
        }
    }
}
