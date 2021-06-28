package com.tokopedia.product_bundle.multiple.di

import com.tokopedia.product_bundle.common.di.ProductBundleComponent
import com.tokopedia.product_bundle.common.di.ProductBundleModule
import com.tokopedia.product_bundle.common.di.ProductBundleViewModelModule
import com.tokopedia.product_bundle.multiple.presentation.MultipleProductBundleFragment
import dagger.Component

@MultipleProductBundleScope
@Component(modules = [ProductBundleModule::class, ProductBundleViewModelModule::class], dependencies = [ProductBundleComponent::class])
interface MultipleProductBundleComponent {
    fun inject(fragment: MultipleProductBundleFragment)
}