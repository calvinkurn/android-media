package com.tokopedia.core.gcm.notification.dedicated;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.base.BaseNotification;

/**
 * Created by alvarisi on 1/13/17.
 */

public class ReviewReplyNotification extends BaseNotification {
    public ReviewReplyNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle data) {
        if (mContext instanceof TkpdCoreRouter) {
            this.mNotificationPass = ((TkpdCoreRouter) mContext).setNotificationPass(mContext,
                    mNotificationPass, data,
                    mContext.getString(R.string.title_reply_review));
        }
    }
}
