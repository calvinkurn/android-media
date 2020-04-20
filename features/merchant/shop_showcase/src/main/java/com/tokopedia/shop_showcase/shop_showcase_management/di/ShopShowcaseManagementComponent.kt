package com.tokopedia.shop_showcase.shop_showcase_management.di

import com.tokopedia.shop_showcase.di.ShopShowcaseComponent
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment.ShopShowcaseListFragment
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment.ShopShowcaseListReorderFragment
import dagger.Component

@ShopShowcaseManagementScope
@Component(modules = [ShopShowcaseManagementModule::class], dependencies = [ShopShowcaseComponent::class])
interface ShopShowcaseManagementComponent {

    fun inject(view: ShopShowcaseListFragment)
    fun inject(view: ShopShowcaseListReorderFragment)

}