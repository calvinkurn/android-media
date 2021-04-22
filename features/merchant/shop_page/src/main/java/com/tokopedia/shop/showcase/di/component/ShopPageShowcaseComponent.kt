package com.tokopedia.shop.showcase.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.showcase.di.module.ShopPageShowcaseModule
import com.tokopedia.shop.showcase.di.scope.ShopPageShowcaseScope
import com.tokopedia.shop.showcase.presentation.fragment.ShopPageShowcaseFragment
import dagger.Component

/**
 * Created by Rafli Syam on 05/03/2021
 */
@ShopPageShowcaseScope
@Component(modules = [ShopPageShowcaseModule::class], dependencies = [BaseAppComponent::class])
interface ShopPageShowcaseComponent {
    fun inject(shopPageShowcaseFragment: ShopPageShowcaseFragment)
}