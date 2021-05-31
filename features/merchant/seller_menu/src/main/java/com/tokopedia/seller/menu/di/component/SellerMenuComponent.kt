package com.tokopedia.seller.menu.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.seller.menu.di.module.SellerMenuModule
import com.tokopedia.seller.menu.di.module.SellerMenuUseCaseModule
import com.tokopedia.seller.menu.di.module.ViewModelModule
import com.tokopedia.seller.menu.di.scope.SellerMenuScope
import com.tokopedia.seller.menu.presentation.activity.SellerMenuActivity
import com.tokopedia.seller.menu.presentation.activity.SellerSettingsActivity
import com.tokopedia.seller.menu.presentation.fragment.AdminRoleAuthorizeFragment
import com.tokopedia.seller.menu.presentation.fragment.SellerMenuFragment
import com.tokopedia.seller.menu.presentation.fragment.SellerSettingsFragment
import dagger.Component

@SellerMenuScope
@Component(
    modules = [
        SellerMenuModule::class,
        SellerMenuUseCaseModule::class,
        ViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface SellerMenuComponent {

    fun inject(fragment: SellerMenuFragment)

    fun inject(fragment: SellerSettingsFragment)

    fun inject(fragment: AdminRoleAuthorizeFragment)

    fun inject(activity: SellerMenuActivity)

    fun inject(activity: SellerSettingsActivity)
}