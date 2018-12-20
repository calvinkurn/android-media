package com.tokopedia.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;

/**
 * Created by meta on 18/07/18.
 */
public interface GlobalNavRouter {

    Fragment getHomeFragment(boolean scroll);

    Fragment getFeedPlusFragment(Bundle bundle);

    Fragment getCartFragment(Bundle bundle);

    Fragment getEmptyCartFragment(String autoApplyMessage, String state, String titleDesc);

    Intent getInboxTalkCallingIntent(Context context);

    Intent getInboxTicketCallingIntent(Context context);

    ApplicationUpdate getAppUpdate(Context context);

    void showHockeyAppDialog(Activity activity);

    Intent getOnBoardingIntent(Activity activity);

    int getCartCount(Context context);

    void setCartCount(Context context, int count);

    void sendAnalyticsFirstTime();

    Intent getShopPageIntent(Context context, String shopID);

    Intent getOpenShopIntent(Context context);

    Intent getHomeIntent(Context context);

    Intent gotoSearchPage(Context context);

    Intent instanceIntentDigitalCategoryList();

    Intent gotoWishlistPage(Context context);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    void sendOpenHomeEvent();
}
