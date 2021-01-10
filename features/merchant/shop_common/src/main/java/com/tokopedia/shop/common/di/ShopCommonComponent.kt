package com.tokopedia.shop.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.common.view.fragment.AdminRoleAuthorizeFragment
import dagger.Component

@ShopCommonScope
@Component(
        modules = [ShopCommonModule::class, ShopCommonViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface ShopCommonComponent {

    fun inject(adminRoleAuthorizeFragment: AdminRoleAuthorizeFragment)

}