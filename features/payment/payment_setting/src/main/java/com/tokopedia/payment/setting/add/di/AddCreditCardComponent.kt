package com.tokopedia.payment.setting.add.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.add.AddCreditCardFragment
import com.tokopedia.payment.setting.authenticate.di.AuthenticateCreditCardModule
import com.tokopedia.payment.setting.authenticate.di.AuthenticateCCScope
import dagger.Component

@AuthenticateCCScope
@Component(modules = arrayOf(AuthenticateCreditCardModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface AddCreditCardComponent{
    fun inject(addCreditCardFragment: AddCreditCardFragment)
}