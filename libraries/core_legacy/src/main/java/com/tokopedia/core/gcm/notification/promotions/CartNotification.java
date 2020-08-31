package com.tokopedia.core.gcm.notification.promotions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;

/**
 * Created by alvarisi on 1/16/17.
 */

public class CartNotification extends BasePromoNotification {
    public CartNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        Intent intentCart = TkpdCoreRouter.createInstanceCartActivity(mContext);
        mNotificationPass.mIntent = NotificationUtils.configurePromoIntent(intentCart, data);
        try {
            mNotificationPass.classParentStack = TkpdCoreRouter.createInstanceCartClass();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        mNotificationPass.title = data.getString(ARG_NOTIFICATION_TITLE, "");
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.isAllowedBigStyle = true;
        mNotificationPass.mIntent.putExtras(data);
    }

    @Override
    protected void showNotification(Bundle inComingBundle) {
        UserSessionInterface userSession = new UserSession(mContext);
        if (userSession.isLoggedIn()) {
            super.showNotification(inComingBundle);
        }
    }
}
