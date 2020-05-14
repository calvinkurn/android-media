package com.tokopedia.productcard.options

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@ProductCardOptionsScope
@Component(modules = [
    ProductCardOptionsViewModelFactoryModule::class
], dependencies = [BaseAppComponent::class])
internal interface ProductCardOptionsComponent {

    fun inject(productCardOptionsActivity: ProductCardOptionsActivity)
}