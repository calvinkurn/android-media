package com.tokopedia.payment.setting.authenticate.di

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.payment.setting.authenticate.view.presenter.AuthenticateCCPresenter
import dagger.Module
import dagger.Provides

@Module
class AuthenticateCreditCardModule {

    @AuthenticateCCScope
    @Provides
    fun providePresenter() : AuthenticateCCPresenter {
        return AuthenticateCCPresenter(GraphqlUseCase())
    }

}
