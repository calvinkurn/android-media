package com.tokopedia.shop.pageheader.di

import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.pageheader.presentation.ShopPageFragment
import dagger.Component

@Component(modules = [ShopPageModule::class], dependencies = [ShopComponent::class])
@ShopPageScope
interface ShopPageComponent {
    fun inject(fragment: ShopPageFragment)
}
