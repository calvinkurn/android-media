package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/16/17.
 */

public class ReputationSmileyNotification extends BaseNotification {
    public ReputationSmileyNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle incomingMessage) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, InboxReputationActivity.class)
        );
        mNotificationPass.classParentStack = InboxReputationActivity.class;
        mNotificationPass.title = mContext.getString(R.string.title_get_reputation);
        mNotificationPass.ticker = incomingMessage.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = incomingMessage.getString(ARG_NOTIFICATION_DESCRIPTION);
    }
}