package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.home.account.domain.GetAccountUseCase;
import com.tokopedia.home.account.presentation.presenter.AccountHomePresenter;
import com.tokopedia.navigation_common.model.WalletPref;

import dagger.Module;
import dagger.Provides;

/**
 * @author okasurya on 7/20/18.
 */
@Module
public class AccountHomeModule {

    @Provides
    WalletPref provideWalletPref(@ApplicationContext Context context, Gson gson){
        return new WalletPref(context, gson);
    }

    @Provides
    AccountHomePresenter provideAccountHomePresenter(
            GetAccountUseCase getAccountUseCase
    ){
        return new AccountHomePresenter(getAccountUseCase);
    }
}
