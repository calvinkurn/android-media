package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.gcm.base.BaseNotification;
import com.tokopedia.core.router.reputation.ReputationRouter;

/**
 * Created by alvarisi on 1/16/17.
 */

public class ReputationSmileyToSellerEditNotification extends BaseNotification {
    public ReputationSmileyToSellerEditNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        if (mContext instanceof ReputationRouter) {
            this.mNotificationPass = ((ReputationRouter) mContext).setNotificationPass(mContext,
                    mNotificationPass, data,
                    mContext.getString(R.string.title_get_edit_reputation));
        }
    }
}
