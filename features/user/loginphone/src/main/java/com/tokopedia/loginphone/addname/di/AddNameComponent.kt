package com.tokopedia.loginphone.addname.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.loginphone.addname.fragment.AddNameRegisterPhoneFragment
import dagger.Component

/**
 * @author by nisie on 23/04/19.
 */
@AddNameScope
@Component(modules = arrayOf(AddNameModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface AddNameComponent {
    fun inject(addCreditCardFragment: AddNameRegisterPhoneFragment)
}