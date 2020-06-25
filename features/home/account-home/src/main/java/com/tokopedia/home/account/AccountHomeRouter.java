package com.tokopedia.home.account;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.home.account.di.AccountHomeInjection;

public interface AccountHomeRouter {

    void doLogoutAccount(Context activity);

    Intent getManageAddressIntent(Context context);

    void goToManageShopShipping(Context context);

    void goToManageShopProduct(Context context);

    void goToSaldo(Context context);

    AccountHomeInjection getAccountHomeInjection();

    String getStringRemoteConfig(String key, String defaultValue);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    Intent getTrainOrderListIntent(Context context);

    void sendAnalyticsUserAttribute(UserAttributeData userAttributeData);

    void goToCreateMerchantRedirect(Context context);

    boolean isEnableInterestPick();
}
