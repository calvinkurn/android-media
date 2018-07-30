package com.tokopedia.home.account.presentation;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.home.account.di.AccountHomeInjection;

public interface AccountHomeRouter {
    void doLogoutAccount(Context context);

    void goToHelpCenter(Context context);

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

    void goToTokoCash(Context context);

    void goToSaldo(Context context);

    AccountHomeInjection getAccountHomeInjection();
}
