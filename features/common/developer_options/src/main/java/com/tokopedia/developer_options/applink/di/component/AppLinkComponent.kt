package com.tokopedia.developer_options.applink.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.developer_options.applink.di.module.AppLinkViewModelModule
import com.tokopedia.developer_options.applink.di.scope.AppLinkScope
import com.tokopedia.developer_options.applink.presentation.fragment.AppLinkListFragment
import dagger.Component

@AppLinkScope
@Component(
    modules = [AppLinkViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface AppLinkComponent {
    fun inject(appLinkListFragment: AppLinkListFragment)
}