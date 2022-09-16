package com.tokopedia.loginregister.redefineregisteremail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.redefineregisteremail.view.registeremail.view.fragment.RedefineRegisterEmailFragment
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.view.fragment.RedefineRegisterInputPhoneFragment
import com.tokopedia.loginregister.redefineregisteremail.view.activity.RedefineRegisterEmailActivity
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
    fun inject(fragment: RedefineRegisterEmailFragment)
    fun inject(fragment: RedefineRegisterInputPhoneFragment)
}