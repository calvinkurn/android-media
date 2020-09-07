package com.tokopedia.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;

/**
 * Created by meta on 18/07/18.
 */
public interface GlobalNavRouter {

    Fragment getHomeFragment(boolean scroll);

    Fragment getFeedPlusFragment(Bundle bundle);

    Fragment getOfficialStoreFragment(Bundle bundle);

    ApplicationUpdate getAppUpdate(Context context);

    Intent getHomeIntent(Context context);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    void sendOpenHomeEvent();
}
