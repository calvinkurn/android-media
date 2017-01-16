package com.tokopedia.core.gcm.notification.promotions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.util.SessionHandler;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;

/**
 * Created by alvarisi on 1/16/17.
 */

public class WishlistNotification extends BasePromoNotification {
    protected WishlistNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configurePromoIntent(
                new Intent(mContext, SimpleHomeRouter.getSimpleHomeActivityClass()),
                data
        );
        mNotificationPass.classParentStack = SimpleHomeRouter.getSimpleHomeActivityClass();
        mNotificationPass.title = data.getString(ARG_NOTIFICATION_TITLE, "");
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.isAllowedBigStyle = true;
        data.putInt(SimpleHomeRouter.FRAGMENT_TYPE, SimpleHomeRouter.WISHLIST_FRAGMENT);
        mNotificationPass.mIntent.putExtras(data);
    }

    @Override
    protected void showNotification(Bundle inComingBundle) {
        if (SessionHandler.isV4Login(mContext)) {
            super.showNotification(inComingBundle);
        }
    }
}
