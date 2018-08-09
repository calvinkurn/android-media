package com.tokopedia.home.account.di.module;

import com.tokopedia.home.account.di.scope.BuyerAccountScope;
import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.presenter.BuyerAccountPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author okasurya on 7/17/18.
 */
@Module
public class BuyerAccountModule {
    @Provides
    @BuyerAccountScope
    BuyerAccount.Presenter provideBuyerAccountPresenter() {
        return new BuyerAccountPresenter();
    }
}
