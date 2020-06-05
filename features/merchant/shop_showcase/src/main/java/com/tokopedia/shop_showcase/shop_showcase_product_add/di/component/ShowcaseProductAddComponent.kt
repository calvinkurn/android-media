package com.tokopedia.shop_showcase.shop_showcase_product_add.di.component

import com.tokopedia.shop_showcase.di.ShopShowcaseComponent
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.module.ShowcaseProductAddModule
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.scope.ShowcaseProductAddScope
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.module.ShowcaseProductAddUseCaseModule
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.fragment.ShopShowcaseProductAddFragment
import dagger.Component

/**
 * @author by Rafli Syam on 2020-03-09
 */

@ShowcaseProductAddScope
@Component(modules = [ShowcaseProductAddModule::class, ShowcaseProductAddUseCaseModule::class], dependencies = [ShopShowcaseComponent::class])
interface ShowcaseProductAddComponent {
    fun inject(shopShowcaseProductAddFragment: ShopShowcaseProductAddFragment)
}