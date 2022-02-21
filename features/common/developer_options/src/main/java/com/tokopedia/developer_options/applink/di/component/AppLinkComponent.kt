package com.tokopedia.developer_options.applink.di.component

import com.tokopedia.developer_options.applink.di.module.AppLinkViewModelModule
import com.tokopedia.developer_options.applink.di.scope.AppLinkScope
import com.tokopedia.developer_options.applink.presentation.fragment.AppLinkListFragment
import dagger.Component

@AppLinkScope
@Component(
    modules = [AppLinkViewModelModule::class]
)
interface AppLinkComponent {
    fun inject(appLinkListFragment: AppLinkListFragment)
}