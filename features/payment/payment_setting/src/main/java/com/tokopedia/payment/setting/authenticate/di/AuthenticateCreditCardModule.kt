package com.tokopedia.payment.setting.authenticate.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.payment.setting.authenticate.view.presenter.AuthenticateCCPresenter
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides

@Module
class AuthenticateCreditCardModule {

    @AuthenticateCCScope
    @Provides
    fun providePresenter(@ApplicationContext context: Context) : AuthenticateCCPresenter {
        return AuthenticateCCPresenter(GraphqlUseCase(), UserSession(context))
    }

}
