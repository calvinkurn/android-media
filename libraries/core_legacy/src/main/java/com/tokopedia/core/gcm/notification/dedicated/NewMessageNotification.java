package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;

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
        if (mContext instanceof TkpdCoreRouter) {
            mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                    ((TkpdCoreRouter)mContext).getInboxMessageIntent(mContext)
            );
            mNotificationPass.classParentStack = TkpdCoreRouter.getInboxMessageActivityClass(mContext);
            mNotificationPass.title = String.format(
                    "%s %s", data.getString("counter"), mContext.getString(R.string.title_new_message));
            mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
            mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        }
    }
}
