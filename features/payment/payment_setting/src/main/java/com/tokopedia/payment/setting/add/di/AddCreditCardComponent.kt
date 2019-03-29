package com.tokopedia.payment.setting.add.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.add.view.fragment.AddCreditCardFragment
import dagger.Component

@AddCreditCardScope
@Component(modules = arrayOf(AddCreditCardModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface AddCreditCardComponent{
    fun inject(addCreditCardFragment: AddCreditCardFragment)
}