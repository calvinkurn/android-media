package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.home.account.di.scope.SellerAccountScope;
import com.tokopedia.home.account.domain.GetSellerAccountUseCase;
import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.presenter.SellerAccountPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alvinatin on 14/08/18.
 */

@Module
public class SellerAccountModule {
    @Provides
    @SellerAccountScope
    SellerAccount.Presenter provideSellerAccountPresenter(GetSellerAccountUseCase getSellerAccountUseCase, UserSessionInterface userSession) {
        return new SellerAccountPresenter(getSellerAccountUseCase, userSession);
    }

    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
