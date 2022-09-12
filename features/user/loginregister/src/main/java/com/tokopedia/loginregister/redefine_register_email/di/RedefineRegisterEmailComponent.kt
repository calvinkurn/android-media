package com.tokopedia.loginregister.redefine_register_email.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.redefine_register_email.view.register_email.view.fragment.RedefineRegisterEmailFragment
import com.tokopedia.loginregister.redefine_register_email.view.input_phone.view.fragment.RedefineRegisterInputPhoneFragment
import com.tokopedia.loginregister.redefine_register_email.view.activity.RedefineRegisterEmailActivity
import dagger.Component

@ActivityScope
@Component(
    modules = [
        RedefineRegisterEmailModule::class,
        RedefineRegisterEmailViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface RedefineRegisterEmailComponent {
    fun inject(activity: RedefineRegisterEmailActivity)
    fun inject(fragment: RedefineRegisterEmailFragment)
    fun inject(fragment: RedefineRegisterInputPhoneFragment)
}