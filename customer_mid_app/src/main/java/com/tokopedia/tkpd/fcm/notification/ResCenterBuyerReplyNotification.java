package com.tokopedia.tkpd.fcm.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core2.R;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;

/**
 * Created by alvarisi on 1/16/17.
 */

public class ResCenterBuyerReplyNotification extends BaseNotification {
    public static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";
    private static final String INBOX_RESCENTER_ACTIVITY = "com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity";

    public ResCenterBuyerReplyNotification(Context context) {
        super(context);
    }

    private static Class<?> getActivityClass(String activityFullPath) throws ClassNotFoundException {
        return Class.forName(activityFullPath);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(
                new Intent(mContext, getInboxResCenterActivityClass())
        );
        mNotificationPass.classParentStack = getInboxResCenterActivityClass();
        mNotificationPass.title = mContext.getString(R.string.title_notif_rescenter);
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_STATE_TAB_POSITION,
                TkpdState.InboxResCenter.RESO_ALL);
        mNotificationPass.extraData = bundle;
        mNotificationPass.mIntent.putExtras(bundle);
    }

    public Class<?> getInboxResCenterActivityClass() {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = getActivityClass(INBOX_RESCENTER_ACTIVITY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
    }
}
