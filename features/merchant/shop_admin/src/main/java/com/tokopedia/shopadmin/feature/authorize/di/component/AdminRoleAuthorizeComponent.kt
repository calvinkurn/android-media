package com.tokopedia.shopadmin.feature.authorize.di.component

import com.tokopedia.shopadmin.common.di.ShopAdminComponent
import com.tokopedia.shopadmin.feature.authorize.di.module.AdminRoleAuthorizeModule
import com.tokopedia.shopadmin.feature.authorize.di.scope.AdminRoleAuthorizeScope
import com.tokopedia.shopadmin.feature.authorize.presentation.fragment.AdminRoleAuthorizeFragment
import dagger.Component

@AdminRoleAuthorizeScope
@Component(
    modules = [
        AdminRoleAuthorizeModule::class
    ],
    dependencies = [
        ShopAdminComponent::class
    ]
)
interface AdminRoleAuthorizeComponent {
    fun inject(fragment: AdminRoleAuthorizeFragment)

}
