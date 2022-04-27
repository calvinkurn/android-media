package com.tokopedia.loginregister.invalid_phone_number.di

import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.invalid_phone_number.view.activity.InvalidPhoneNumberActivity
import com.tokopedia.loginregister.invalid_phone_number.view.fragment.InvalidPhoneNumberFragment
import dagger.Component

@InvalidPhoneNumberScope
@Component(
    modules = [
        InvalidPhoneNumberModule::class,
        InvalidPhoneNumberViewModelModule::class,
        InteractorModule::class
    ],
    dependencies = [LoginRegisterComponent::class]
)
interface InvalidPhoneNumberComponent {
    fun inject(activity: InvalidPhoneNumberActivity)
    fun inject(fragment: InvalidPhoneNumberFragment)
}