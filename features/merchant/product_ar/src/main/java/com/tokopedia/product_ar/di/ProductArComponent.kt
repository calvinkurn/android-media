package com.tokopedia.product_ar.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product_ar.view.fragment.ProductArComparisonFragment
import com.tokopedia.product_ar.view.fragment.ProductArFragment
import dagger.Component

@ProductArScope
@Component(
    modules = [ProductArModule::class, ProductArViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ProductArComponent {
    fun inject(fragment: ProductArFragment)
    fun inject(fragment: ProductArComparisonFragment)
}