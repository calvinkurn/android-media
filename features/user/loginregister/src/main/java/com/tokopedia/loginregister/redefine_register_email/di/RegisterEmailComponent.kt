package com.tokopedia.loginregister.redefine_register_email.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.redefine_register_email.view.fragment.RedefineRegisterEmailFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [
        RegisterEmailViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface RegisterEmailComponent {
    fun inject(fragment: RedefineRegisterEmailFragment)
}