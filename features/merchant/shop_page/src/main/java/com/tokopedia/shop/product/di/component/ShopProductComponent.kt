package com.tokopedia.shop.product.di.component

import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.di.scope.ShopProductScope
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment
import com.tokopedia.shop.product.view.fragment.ShopPageProductListResultFragment
import dagger.Component

/**
 * Created by hendry on 18/01/18.
 */
@ShopProductScope
@Component(modules = [ShopProductModule::class], dependencies = [ShopComponent::class])
interface ShopProductComponent {
    fun inject(shopPageProductFragment: ShopPageProductListFragment?)
    fun inject(shopPageProductListResultFragment: ShopPageProductListResultFragment?)

    fun inject(shopProductListResultActivity: ShopProductListResultActivity?)

}
