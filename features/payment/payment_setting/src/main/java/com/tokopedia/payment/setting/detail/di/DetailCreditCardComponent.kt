package com.tokopedia.payment.setting.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.authenticate.di.AuthenticateCreditCardModule
import com.tokopedia.payment.setting.detail.DetailCreditCardFragment
import dagger.Component

@DetailCreditCardScope
@Component(modules = arrayOf(AuthenticateCreditCardModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface DetailCreditCardComponent{
    fun inject(detailCreditCardFragment: DetailCreditCardFragment)
}