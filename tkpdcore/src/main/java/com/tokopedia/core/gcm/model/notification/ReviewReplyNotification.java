package com.tokopedia.core.gcm.model.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/13/17.
 */

public class ReviewReplyNotification extends BaseNotification {
    protected ReviewReplyNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, InboxReputationActivity.class)
        );
        mNotificationPass.classParentStack = InboxReputationActivity.class;
        mNotificationPass.title = mContext.getString(R.string.title_reply_review);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
    }
}
