package com.tokopedia.tkpd.tkpdreputation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.gcm.model.NotificationPass;

import java.util.HashMap;

/**
 * @author by nisie on 9/20/17.
 */

public interface ReputationRouter {
    Intent getInboxReputationIntent(Context context);

    Fragment getReputationHistoryFragment();

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);
  
    void sendEventTrackingShopPage(HashMap<String, Object> eventTracking);

    Intent getTopProfileIntent(Context context, String reviewUserId);

}
