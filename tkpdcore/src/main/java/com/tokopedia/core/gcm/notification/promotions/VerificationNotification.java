package com.tokopedia.core.gcm.notification.promotions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_TITLE;

/**
 * Created by alvarisi on 1/16/17.
 */

public class VerificationNotification extends BasePromoNotification {
    public VerificationNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        if (SessionHandler.isV4Login(mContext)) {
            mNotificationPass.mIntent = NotificationUtils.configurePromoIntent(
                    new Intent(mContext, ManageGeneral.class),
                    data
            );
        } else {
            mNotificationPass.mIntent = NotificationUtils.configurePromoIntent(
                    new Intent(mContext, OldSessionRouter.getLoginActivityClass()),
                    data
            );
            data.putInt("keylogin1", TkpdState.DrawerPosition.LOGIN);
            data.putInt("keylogin2", SessionView.HOME);
        }

        mNotificationPass.classParentStack = SimpleHomeRouter.getSimpleHomeActivityClass();
        mNotificationPass.title = data.getString(ARG_NOTIFICATION_TITLE, "");
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION, "");
        mNotificationPass.isAllowedBigStyle = true;
        mNotificationPass.mIntent.putExtras(data);
    }
}
