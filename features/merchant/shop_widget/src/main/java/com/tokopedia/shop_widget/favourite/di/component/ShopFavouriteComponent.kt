package com.tokopedia.shop_widget.favourite.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop_widget.favourite.di.module.ShopFavouriteModule
import com.tokopedia.shop_widget.favourite.di.scope.ShopFavouriteScope
import com.tokopedia.shop_widget.favourite.view.activity.ShopFavouriteListActivity
import com.tokopedia.shop_widget.favourite.view.fragment.ShopFavouriteListFragment
import dagger.Component

/**
 * Created by hendry on 18/01/18.
 */
@ShopFavouriteScope
@Component(modules = [ShopFavouriteModule::class], dependencies = [BaseAppComponent::class])
interface ShopFavouriteComponent {
    fun inject(shopFavouriteListActivity: ShopFavouriteListActivity?)
    fun inject(shopFavouriteListFragment: ShopFavouriteListFragment?)
}