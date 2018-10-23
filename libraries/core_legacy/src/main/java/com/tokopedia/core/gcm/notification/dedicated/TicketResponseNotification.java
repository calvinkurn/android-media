package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/13/17.
 */

public class TicketResponseNotification extends BaseNotification {
    public TicketResponseNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                TkpdCoreRouter.getInboxTicketActivityIntent(mContext)
        );
        mNotificationPass.classParentStack = TkpdCoreRouter.getInboxticketActivityClass();
        mNotificationPass.title = mContext.getString(R.string.title_new_ticket);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
    }
}
