package com.tokopedia.seller.menu.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.seller.menu.common.view.fragment.AdminRoleAuthorizeFragment
import dagger.Component

@SellerMenuCommonScope
@Component(
        modules = [SellerMenuCommonModule::class, SellerMenuCommonViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface SellerMenuCommonComponent {

    fun inject(adminRoleAuthorizeFragment: AdminRoleAuthorizeFragment)

}