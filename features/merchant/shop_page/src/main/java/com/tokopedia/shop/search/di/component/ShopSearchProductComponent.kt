package com.tokopedia.shop.search.di.component

import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.search.di.module.ShopSearchProductModule
import com.tokopedia.shop.search.di.scope.ShopSearchProductScope
import com.tokopedia.shop.search.view.fragment.ShopSearchProductFragment
import dagger.Component

@ShopSearchProductScope
@Component(
    modules = [ShopSearchProductModule::class],
    dependencies = [ShopComponent::class]
)
interface ShopSearchProductComponent {
    fun inject(shopSearchProductFragment: ShopSearchProductFragment)
}
