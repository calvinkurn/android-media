package com.tokopedia.tkpd.tkpdreputation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * @author by nisie on 9/20/17.
 */

public interface ReputationRouter {
    Intent getInboxReputationIntent(Context context);

    Fragment getReputationHistoryFragment();

    Intent getLoginIntent(Context context);

    Intent getTopProfileIntent(Context context, String reviewUserId);
}
