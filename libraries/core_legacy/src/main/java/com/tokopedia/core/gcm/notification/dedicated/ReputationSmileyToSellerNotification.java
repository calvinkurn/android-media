package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.base.BaseNotification;

/**
 * Created by alvarisi on 1/16/17.
 */

public class ReputationSmileyToSellerNotification extends BaseNotification {
    public ReputationSmileyToSellerNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle incomingMessage) {
        if (mContext instanceof TkpdCoreRouter) {
            this.mNotificationPass = ((TkpdCoreRouter) mContext).setNotificationPass(mContext,
                    mNotificationPass, incomingMessage,
                    mContext.getString(R.string.title_get_reputation));
        }
    }
}
