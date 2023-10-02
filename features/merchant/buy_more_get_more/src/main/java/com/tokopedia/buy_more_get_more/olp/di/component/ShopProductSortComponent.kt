package com.tokopedia.buy_more_get_more.di.component

import com.tokopedia.buy_more_get_more.di.module.ShopProductSortModule
import com.tokopedia.buy_more_get_more.di.scope.ShopProductSortScope
import com.tokopedia.buy_more_get_more.sort.fragment.ShopProductSortFragment
import dagger.Component


@ShopProductSortScope
@Component(modules = [ShopProductSortModule::class])
interface ShopProductSortComponent {

    fun inject(shopProductFilterFragment: ShopProductSortFragment)

}
