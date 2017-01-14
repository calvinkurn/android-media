package com.tokopedia.core.gcm.model.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.inboxmessage.activity.InboxMessageActivity;
import com.tokopedia.core.talk.inboxtalk.activity.InboxTalkActivity;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/12/17.
 */

public class NewDiscussionNotification extends BaseNotification {

    public NewDiscussionNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle incomingMessage) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, InboxMessageActivity.class)
        );
        mNotificationPass.classParentStack = InboxTalkActivity.class;
        mNotificationPass.title = String.format(
                "%s %s",
                incomingMessage.getString("counter"),
                mContext.getString(R.string.title_new_talk)
        );
        mNotificationPass.ticker = incomingMessage.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = incomingMessage.getString(ARG_NOTIFICATION_DESCRIPTION);
    }
}
