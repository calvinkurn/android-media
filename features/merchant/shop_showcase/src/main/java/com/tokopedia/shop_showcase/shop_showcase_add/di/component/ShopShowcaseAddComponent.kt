package com.tokopedia.shop_showcase.shop_showcase_add.di.component

import com.tokopedia.shop_showcase.di.ShopShowcaseComponent
import com.tokopedia.shop_showcase.shop_showcase_add.di.modules.ShopShowcaseAddModule
import com.tokopedia.shop_showcase.shop_showcase_add.di.scope.ShopShowcaseAddScope
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.fragment.ShopShowcaseAddFragment
import dagger.Component

/**
 * @author by Rafli Syam on 2020-03-09
 */

@ShopShowcaseAddScope
@Component(modules = [ShopShowcaseAddModule::class], dependencies = [ShopShowcaseComponent::class])
interface ShopShowcaseAddComponent {
    fun inject(shopShowcaseAddFragment: ShopShowcaseAddFragment)
}