package com.tokopedia.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by meta on 18/07/18.
 */
public interface GlobalNavRouter {

    Fragment getHomeFragment();

    Fragment getFeedPlusFragment();

    Fragment getCartFragment();

    Intent getInboxTalkCallingIntent(Context context);

    Intent getInboxTicketCallingIntent(Context context);

    Intent getSellerInfoCallingIntent(Context context);

    Intent getResolutionCenterIntentBuyer(Context context);

    Intent getResolutionCenterIntentSeller(Context context);
}
