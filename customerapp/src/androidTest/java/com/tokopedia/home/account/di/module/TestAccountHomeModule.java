package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.home.account.domain.GetAccountUseCase;
import com.tokopedia.home.account.presentation.presenter.AccountHomePresenter;
import com.tokopedia.navigation_common.model.WalletPref;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * @author okasurya on 7/20/18.
 */
@Module
public class TestAccountHomeModule {
    private WalletPref walletPref;
    private GetAccountUseCase getAccountUseCase;

    @Provides
    WalletPref provideWalletPref(@ApplicationContext Context context, Gson gson){
        return walletPref == null ? (walletPref = mock(WalletPref.class)) : walletPref;
    }

    @Provides
    AccountHomePresenter provideAccountHomePresenter(
            GetAccountUseCase getAccountUseCase
    ){

        if(this.getAccountUseCase==null){
            this.getAccountUseCase = mock(GetAccountUseCase.class);
        }
        return new AccountHomePresenter(this.getAccountUseCase);
    }

    public WalletPref getWalletPref() {
        return walletPref;
    }

    public GetAccountUseCase getGetAccountUseCase() {
        return getAccountUseCase;
    }
}
