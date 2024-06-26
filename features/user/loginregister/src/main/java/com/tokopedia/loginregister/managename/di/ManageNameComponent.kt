package com.tokopedia.loginregister.managename.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.loginregister.managename.view.fragment.AddNameFragment
import com.tokopedia.sessioncommon.di.SessionCommonScope
import dagger.Component

@ManageNameScope
@SessionCommonScope
@Component(
        modules = [
            ManageNameModule::class,
            ManageNameQueryModule::class,
            ManageNameViewModelModule::class
        ],
        dependencies = [BaseAppComponent::class]
)
interface ManageNameComponent {
    fun inject(fragment: AddNameFragment)
}
