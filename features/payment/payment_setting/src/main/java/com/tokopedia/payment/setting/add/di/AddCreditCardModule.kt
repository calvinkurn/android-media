package com.tokopedia.payment.setting.add.di

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.payment.setting.add.AddCreditCardPresenter
import com.tokopedia.payment.setting.add.domain.AddCreditCardUseCase
import com.tokopedia.payment.setting.authenticate.di.AuthenticateCCScope
import dagger.Module
import dagger.Provides

@Module
class AddCreditCardModule {

    @AuthenticateCCScope
    @Provides
    fun providePresenter(addCreditCardUseCase: AddCreditCardUseCase) : AddCreditCardPresenter{
        return AddCreditCardPresenter(addCreditCardUseCase)
    }

    @AuthenticateCCScope
    @Provides
    fun productAddCreditCardUseCase() : AddCreditCardUseCase{
        return AddCreditCardUseCase()
    }

}
