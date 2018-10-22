package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core2.R;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.base.BaseNotification;

/**
 * Created by alvarisi on 1/16/17.
 */

public class ReputationSmileyNotification extends BaseNotification {
    public ReputationSmileyNotification(Context context) {
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