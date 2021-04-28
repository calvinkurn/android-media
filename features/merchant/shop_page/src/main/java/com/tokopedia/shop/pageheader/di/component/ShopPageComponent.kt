package com.tokopedia.shop.pageheader.di.component

import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.pageheader.di.module.ShopPageModule
import com.tokopedia.shop.pageheader.di.scope.ShopPageScope
import com.tokopedia.shop.pageheader.presentation.fragment.NewShopPageFragment
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageFragment
import dagger.Component

/**
 * Created by hendry on 18/01/18.
 */
@ShopPageScope
@Component(modules = [ShopPageModule::class], dependencies = [ShopComponent::class])
interface ShopPageComponent {
    fun inject(shopPageFragment: ShopPageFragment?)
    fun inject(fragment: NewShopPageFragment?)
}