package com.tokopedia.home.account.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.home.account.di.AccountHomeInjection;

// TODO: 8/8/18 move router to parent folder
public interface AccountHomeRouter {

    AnalyticTracker getAnalyticTracker();

    void doLogoutAccount(Activity activity);

    void goToHelpCenter(Context context);

    Intent getIntentCreateShop(Context context);

    Intent getManageProfileIntent(Context context);

    Intent getManagePasswordIntent(Context context);

    Intent getManageAddressIntent(Context context);

    void goToShopEditor(Context context);

    void goToManageShopShipping(Context context);

    void goToManageShopEtalase(Context context);

    void goTotManageShopNotes(Context context);

    void goToManageShopLocation(Context context);

    void goToManageShopProduct(Context context);

    void goToManageBankAccount(Context context);

    void goToManageCreditCard(Context context);

    void goToTokoCash(String applinkUrl, String redirectUrl, Activity activity);

    void goToSaldo(Context context);

    AccountHomeInjection getAccountHomeInjection();

    Fragment getFavoriteFragment();

    void gotoTopAdsDashboard(Context context);

    void goToGMSubscribe(Context context);

    String getStringRemoteConfig(String key, String defaultValue);

    Intent getTrainOrderListIntent(Context context);
}
