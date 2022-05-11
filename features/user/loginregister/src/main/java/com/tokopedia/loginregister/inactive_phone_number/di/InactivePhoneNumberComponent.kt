package com.tokopedia.loginregister.inactive_phone_number.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.common.view.bottomsheet.InactivePhoneNumberBottomSheet
import com.tokopedia.loginregister.inactive_phone_number.view.activity.InactivePhoneNumberActivity
import com.tokopedia.loginregister.inactive_phone_number.view.fragment.InactivePhoneNumberFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [
        InactivePhoneNumberModule::class,
        InactivePhoneNumberViewModelModule::class
    ],
    dependencies = [LoginRegisterComponent::class]
)
interface InactivePhoneNumberComponent {
    fun inject(activity: InactivePhoneNumberActivity)
    fun inject(fragment: InactivePhoneNumberFragment)
    fun inject(bottomSheet: InactivePhoneNumberBottomSheet)
}