package com.tokopedia.payment.setting.authenticate.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.authenticate.view.fragment.AuthenticateCreditCardFragment
import dagger.Component

@AuthenticateCCScope
@Component(modules = arrayOf(AuthenticateCreditCardModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface AuthenticateCreditCardComponent{
    fun inject(authenticateCreditCardFragment: AuthenticateCreditCardFragment)
}