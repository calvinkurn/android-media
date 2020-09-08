package com.tokopedia.productcard.options

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.productcard.options.di.ProductCardOptionsContextModule
import dagger.Component

@ProductCardOptionsScope
@Component(modules = [
    ProductCardOptionsContextModule::class,
    ProductCardOptionsViewModelFactoryModule::class
], dependencies = [BaseAppComponent::class])
internal interface ProductCardOptionsComponent {

    fun inject(productCardOptionsActivity: ProductCardOptionsActivity)
}