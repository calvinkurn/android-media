package com.tokopedia.core.router.reputation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.gcm.model.NotificationPass;

/**
 * @author by nisie on 9/20/17.
 */

public interface ReputationRouter {
    Intent getInboxReputationIntent(Context context);

    NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass,
                                         Bundle data, String notifTitle);

    Intent getInboxReputationHistoryIntent();

    Fragment getReputationHistoryFragment();

    android.app.Fragment getShopReputationFragment();
}
