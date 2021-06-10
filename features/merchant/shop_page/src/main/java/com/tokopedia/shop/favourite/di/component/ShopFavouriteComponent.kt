package com.tokopedia.shop.favourite.di.component

import com.tokopedia.shop.favourite.di.scope.ShopFavouriteScope
import com.tokopedia.shop.favourite.di.module.ShopFavouriteModule
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.favourite.view.fragment.ShopFavouriteListFragment
import dagger.Component

/**
 * Created by hendry on 18/01/18.
 */
@ShopFavouriteScope
@Component(modules = [ShopFavouriteModule::class], dependencies = [ShopComponent::class])
interface ShopFavouriteComponent {
    fun inject(shopFavouriteListFragment: ShopFavouriteListFragment?)
}