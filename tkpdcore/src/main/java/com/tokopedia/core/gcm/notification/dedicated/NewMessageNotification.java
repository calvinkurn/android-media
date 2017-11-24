package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.TkpdInboxRouter;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * @author by alvarisi on 1/12/17.
 */

public class NewMessageNotification extends BaseNotification {

    public NewMessageNotification(Context context) {
        super(context);
    }

    @Override
    public void configureNotificationData(Bundle data) {
        if (MainApplication.getAppContext() instanceof TkpdInboxRouter) {
            mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                    ((TkpdInboxRouter) MainApplication.getAppContext()).getInboxMessageIntent(mContext)
            );
            mNotificationPass.classParentStack = InboxRouter.getInboxMessageActivityClass();
            mNotificationPass.title = String.format(
                    "%s %s", data.getString("counter"), mContext.getString(R.string.title_new_message));
            mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
            mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        }
    }
}
