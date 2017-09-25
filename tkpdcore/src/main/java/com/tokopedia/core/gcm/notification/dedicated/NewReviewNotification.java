package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.router.reputation.ReputationRouter;

/**
 * @author by alvarisi on 1/12/17.
 */

public class NewReviewNotification extends BaseNotification {

    public NewReviewNotification(Context context) {
        super(context);
    }

    @Override
    public void configureNotificationData(Bundle data) {
        if (mContext instanceof ReputationRouter) {
            this.mNotificationPass = ((ReputationRouter) mContext).setNotificationPass(mContext,
                    mNotificationPass, data, getNotifTitle(data));
        }
    }

    public String getNotifTitle(Bundle data) {
        return String.format("%s %s", data.getString("counter"),
                mContext.getString(R.string.title_new_review));
    }
}
