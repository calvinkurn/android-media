package com.tokopedia.shop.pageheader.di.component

import com.tokopedia.creation.common.di.ContentCreationViewModelModule
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.pageheader.di.module.ShopPageBindModule
import com.tokopedia.shop.pageheader.di.module.ShopPageHeaderModule
import com.tokopedia.shop.pageheader.di.scope.ShopPageHeaderScope
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageHeaderFragment
import dagger.Component

/**
 * Created by hendry on 18/01/18.
 */
@ShopPageHeaderScope
@Component(
    modules = [
        ShopPageHeaderModule::class,
        ShopPageBindModule::class,
        ContentCreationViewModelModule::class
    ],
    dependencies = [ShopComponent::class]
)
interface ShopPageHeaderComponent {
    fun inject(headerFragment: ShopPageHeaderFragment?)
}
