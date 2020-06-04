package com.tokopedia.payment.setting.authenticate.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.authenticate.view.fragment.AuthenticateCreditCardFragment
import dagger.Component

@AuthenticateCCScope
@Component(modules = [AuthenticateCreditCardModule::class, GqlQueryModule::class],
        dependencies = [BaseAppComponent::class])
interface AuthenticateCreditCardComponent {
    fun inject(authenticateCreditCardFragment: AuthenticateCreditCardFragment)
}