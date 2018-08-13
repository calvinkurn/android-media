package com.tokopedia.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;

/**
 * Created by meta on 18/07/18.
 */
public interface GlobalNavRouter {

    Fragment getHomeFragment();

    Fragment getFeedPlusFragment(Bundle bundle);

    Fragment getCartFragment();

    Intent getInboxTalkCallingIntent(Context context);

    Intent getInboxTicketCallingIntent(Context context);

    ApplicationUpdate getAppUpdate(Context context);

    void showHockeyAppDialog(Activity activity);

    Intent getOnBoardingIntent(Activity activity);
}
