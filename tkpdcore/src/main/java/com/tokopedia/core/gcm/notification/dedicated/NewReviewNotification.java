package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.router.InboxRouter;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * @author by alvarisi on 1/12/17.
 */

public class NewReviewNotification extends BaseNotification {

    public NewReviewNotification(Context context) {
        super(context);
    }

    @Override
    public void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, InboxReputationActivity.class)
        );
        mNotificationPass.classParentStack= InboxReputationActivity.class;
        mNotificationPass.title = String.format("%s %s", data.getString("counter"), mContext.getString(R.string.title_new_review));
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
    }
}
