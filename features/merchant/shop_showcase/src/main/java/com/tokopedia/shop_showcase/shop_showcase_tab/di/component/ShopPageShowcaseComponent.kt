package com.tokopedia.shop_showcase.shop_showcase_tab.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop_showcase.shop_showcase_tab.di.module.ShopPageShowcaseModule
import com.tokopedia.shop_showcase.shop_showcase_tab.di.scope.ShopPageShowcaseScope
import com.tokopedia.shop_showcase.shop_showcase_tab.presentation.fragment.ShopPageShowcaseFragment
import dagger.Component

/**
 * Created by Rafli Syam on 05/03/2021
 */
@ShopPageShowcaseScope
@Component(modules = [ShopPageShowcaseModule::class], dependencies = [BaseAppComponent::class])
interface ShopPageShowcaseComponent {
    fun inject(shopPageShowcaseFragment: ShopPageShowcaseFragment)
}