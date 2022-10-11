package com.tokopedia.shopadmin.feature.redirection.di.component

import com.tokopedia.shopadmin.common.di.ShopAdminComponent
import com.tokopedia.shopadmin.feature.redirection.di.module.ShopAdminRedirectionModule
import com.tokopedia.shopadmin.feature.redirection.di.scope.ShopAdminRedirectionScope
import com.tokopedia.shopadmin.feature.redirection.presentation.fragment.ShopAdminRedirectionFragment
import dagger.Component

@ShopAdminRedirectionScope
@Component(modules = [ShopAdminRedirectionModule::class], dependencies = [ShopAdminComponent::class])
interface ShopAdminRedirectionComponent {
    fun inject(fragment: ShopAdminRedirectionFragment)
}