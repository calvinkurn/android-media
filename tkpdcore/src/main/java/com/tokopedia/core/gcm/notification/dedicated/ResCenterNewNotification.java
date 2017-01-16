package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.InboxRouter;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/13/17.
 */

public class ResCenterNewNotification extends BaseNotification {
    public ResCenterNewNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, InboxRouter.getInboxResCenterActivityClass())
        );
        mNotificationPass.classParentStack = InboxRouter.getInboxResCenterActivityClass();
        mNotificationPass.title = mContext.getString(R.string.title_new_rescenter);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
    }
}
