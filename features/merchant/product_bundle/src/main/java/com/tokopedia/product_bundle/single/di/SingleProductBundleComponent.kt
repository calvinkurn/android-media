package com.tokopedia.product_bundle.single.di

import com.tokopedia.product_bundle.common.di.ProductBundleComponent
import com.tokopedia.product_bundle.common.di.ProductBundleModule
import com.tokopedia.product_bundle.single.presentation.SingleProductBundleFragment
import dagger.Component

@SingleProductBundleScope
@Component(modules = [ProductBundleModule::class, SingleProductBundleModule::class], dependencies = [ProductBundleComponent::class])
interface SingleProductBundleComponent {
    fun inject(singleProductBundleFragment: SingleProductBundleFragment)
}