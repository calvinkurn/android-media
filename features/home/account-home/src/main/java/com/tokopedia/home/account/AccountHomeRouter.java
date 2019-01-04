package com.tokopedia.home.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.home.account.di.AccountHomeInjection;
import com.tokopedia.navigation_common.model.PendingCashbackModel;
import com.tokopedia.navigation_common.model.WalletModel;

import rx.Observable;

public interface AccountHomeRouter {

    void doLogoutAccount(Activity activity);

    void goToHelpCenter(Context context);

    Intent getIntentCreateShop(Context context);

    Intent getManageProfileIntent(Context context);

    Intent getManageAddressIntent(Context context);

    void goToShopEditor(Context context);

    void goToManageShopShipping(Context context);

    void goToManageShopEtalase(Context context);

    void goTotManageShopNotes(Context context);

    void goToManageShopLocation(Context context);

    void goToManageShopProduct(Context context);

    Intent getSettingBankIntent(Context context);

    void goToManageCreditCard(Context context);

    void goToTokoCash(String applinkUrl, Activity activity);

    void goToSaldo(Context context);

    AccountHomeInjection getAccountHomeInjection();

    Fragment getFavoriteFragment();

    void gotoTopAdsDashboard(Context context);

    void goToGMSubscribe(Context context);

    String getStringRemoteConfig(String key, String defaultValue);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    Intent getTrainOrderListIntent(Context context);

    void sendAnalyticsUserAttribute(UserAttributeData userAttributeData);

    void goToCreateMerchantRedirect(Context context);

    void setPromoPushPreference(Boolean newValue);

    Observable<WalletModel> getTokoCashAccountBalance();

    boolean isEnableInterestPick();
  
    Intent getMitraToppersActivityIntent(Context context);

    Intent getAddPasswordIntent(Context context);
}
