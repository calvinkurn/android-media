package com.tokopedia.home.account.di.module;

import com.tokopedia.home.account.di.scope.SellerAccountScope;
import com.tokopedia.home.account.domain.GetSellerAccountUseCase;
import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.presenter.SellerAccountPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alvinatin on 14/08/18.
 */

@Module
public class SellerAccountModule {
    @Provides
    @SellerAccountScope
    SellerAccount.Presenter provideSellerAccountPresenter(GetSellerAccountUseCase getSellerAccountUseCase) {
        return new SellerAccountPresenter(getSellerAccountUseCase);
    }
}
