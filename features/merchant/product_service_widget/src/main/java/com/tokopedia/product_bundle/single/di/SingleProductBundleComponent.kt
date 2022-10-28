package com.tokopedia.product_bundle.single.di

import com.tokopedia.product_bundle.common.di.ProductBundleComponent
import com.tokopedia.product_bundle.common.di.ProductBundleModule
import com.tokopedia.product_bundle.single.presentation.fragment.SingleProductBundleFragment
import dagger.Component

@SingleProductBundleScope
@Component(modules = [ProductBundleModule::class, SingleProductBundleViewModelModule::class],
    dependencies = [ProductBundleComponent::class])
interface SingleProductBundleComponent {
    fun inject(singleProductBundleFragment: SingleProductBundleFragment)
}