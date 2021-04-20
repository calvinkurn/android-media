package com.tokopedia.product.estimasiongkir.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.estimasiongkir.view.bottomsheet.ProductDetailShippingBottomSheet
import com.tokopedia.product.estimasiongkir.view.fragment.RatesEstimationDetailFragment

import dagger.Component

@RatesEstimationScope
@Component(modules = arrayOf(RatesEstimationModule::class, RatesEstimationViewModelModule::class),
        dependencies = arrayOf(BaseAppComponent::class))
interface RatesEstimationComponent {
    fun inject(fragment: RatesEstimationDetailFragment)
    fun inject(productDetailBottomSheet: ProductDetailShippingBottomSheet)
}
