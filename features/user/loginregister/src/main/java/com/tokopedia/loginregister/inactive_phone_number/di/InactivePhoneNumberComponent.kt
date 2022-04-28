package com.tokopedia.loginregister.inactive_phone_number.di

import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.inactive_phone_number.view.activity.InactivePhoneNumberActivity
import com.tokopedia.loginregister.inactive_phone_number.view.fragment.InactivePhoneNumberFragment
import dagger.Component

@InactivePhoneNumberScope
@Component(
    modules = [
        InactivePhoneNumberModule::class,
        InactivePhoneNumberViewModelModule::class,
        InteractorModule::class
    ],
    dependencies = [LoginRegisterComponent::class]
)
interface InactivePhoneNumberComponent {
    fun inject(activity: InactivePhoneNumberActivity)
    fun inject(fragment: InactivePhoneNumberFragment)
}