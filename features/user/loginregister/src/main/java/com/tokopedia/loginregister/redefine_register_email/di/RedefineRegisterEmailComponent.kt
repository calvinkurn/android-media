package com.tokopedia.loginregister.redefine_register_email.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.redefine_register_email.view.fragment.RedefineRegisterEmailFragment
import com.tokopedia.loginregister.redefine_register_email.view.fragment.RedefineRegisterInputPhoneFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [
        RedefineRegisterEmailViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface RedefineRegisterEmailComponent {
    fun inject(fragment: RedefineRegisterEmailFragment)
    fun inject(fragment: RedefineRegisterInputPhoneFragment)
}