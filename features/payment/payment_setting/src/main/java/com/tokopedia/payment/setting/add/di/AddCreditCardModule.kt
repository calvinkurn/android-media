package com.tokopedia.payment.setting.add.di

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.payment.setting.add.AddCreditCardPresenter
import com.tokopedia.payment.setting.add.domain.AddCreditCardUseCase
import dagger.Module
import dagger.Provides

@Module
class AddCreditCardModule {

    @AddCreditCardScope
    @Provides
    fun providePresenter(addCreditCardUseCase: AddCreditCardUseCase) : AddCreditCardPresenter{
        return AddCreditCardPresenter(addCreditCardUseCase)
    }

    @AddCreditCardScope
    @Provides
    fun productAddCreditCardUseCase() : AddCreditCardUseCase{
        return AddCreditCardUseCase()
    }

    @AddCreditCardScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context) : UserSession{
        return (context as AbstractionRouter).session
    }

}
