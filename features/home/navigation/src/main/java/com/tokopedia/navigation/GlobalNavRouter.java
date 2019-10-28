package com.tokopedia.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;

/**
 * Created by meta on 18/07/18.
 */
public interface GlobalNavRouter {

    Fragment getHomeFragment(boolean scroll);

    Fragment getFeedPlusFragment(Bundle bundle);

    Fragment getCartFragment(Bundle bundle);

    Fragment getOfficialStoreFragment(Bundle bundle);

    Intent getInboxTalkCallingIntent(Context context);

    Intent getInboxTicketCallingIntent(Context context);

    ApplicationUpdate getAppUpdate(Context context);

    int getCartCount(Context context);

    void setCartCount(Context context, int count);

    void sendAnalyticsFirstTime();

    Intent getShopPageIntent(Context context, String shopID);

    Intent getHomeIntent(Context context);

    Intent instanceIntentDigitalCategoryList();

    Intent gotoWishlistPage(Context context);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    void sendOpenHomeEvent();

    void setCategoryAbTestingConfig();
}
