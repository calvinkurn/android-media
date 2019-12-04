package com.tokopedia.saldodetails.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.saldodetails.presenter.SaldoHoldInfoPresenter;
import com.tokopedia.saldodetails.usecase.GetHoldInfoUsecase;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;

@Module
public class SaldoDetailsModule {

    @SaldoDetailsScope
    @Provides
    UserSession providesUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @SaldoDetailsScope
    @Provides
    GetHoldInfoUsecase getHoldInfoUsecase(@ApplicationContext Context context) {
        return new GetHoldInfoUsecase(context, new GraphqlUseCase());
    }

    @SaldoDetailsScope
    @Provides
    SaldoHoldInfoPresenter saldoHoldInfoPresenter(GetHoldInfoUsecase usecase) {
        return new SaldoHoldInfoPresenter(usecase);
    }

}
