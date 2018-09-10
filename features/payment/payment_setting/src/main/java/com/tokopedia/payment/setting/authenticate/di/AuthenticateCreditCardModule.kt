package com.tokopedia.payment.setting.authenticate.di

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.payment.setting.add.AddCreditCardPresenter
import com.tokopedia.payment.setting.add.domain.AddCreditCardUseCase
import com.tokopedia.payment.setting.authenticate.AuthenticateCCPresenter
import dagger.Module
import dagger.Provides

@Module
class AuthenticateCreditCardModule {

    @AuthenticateCCScope
    @Provides
    fun providePresenter() : AuthenticateCCPresenter{
        return AuthenticateCCPresenter(GraphqlUseCase())
    }

}
