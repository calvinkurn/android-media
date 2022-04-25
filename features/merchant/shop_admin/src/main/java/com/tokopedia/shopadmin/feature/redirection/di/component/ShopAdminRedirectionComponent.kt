package com.tokopedia.shopadmin.feature.redirection.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shopadmin.feature.redirection.di.module.ShopAdminRedirectionModule
import com.tokopedia.shopadmin.feature.redirection.di.scope.ShopAdminRedirectionScope
import com.tokopedia.shopadmin.feature.redirection.presentation.fragment.ShopAdminRedirectionFragment
import dagger.Component

@ShopAdminRedirectionScope
@Component(modules = [ShopAdminRedirectionModule::class], dependencies = [BaseAppComponent::class])
interface ShopAdminRedirectionComponent {
    fun inject(fragment: ShopAdminRedirectionFragment)
}