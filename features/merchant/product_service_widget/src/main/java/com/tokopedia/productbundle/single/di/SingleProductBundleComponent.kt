package com.tokopedia.productbundle.single.di

import com.tokopedia.productbundle.common.di.ProductBundleComponent
import com.tokopedia.productbundle.common.di.ProductBundleModule
import com.tokopedia.productbundle.single.presentation.fragment.SingleProductBundleFragment
import dagger.Component

@SingleProductBundleScope
@Component(modules = [ProductBundleModule::class, SingleProductBundleViewModelModule::class],
    dependencies = [ProductBundleComponent::class])
interface SingleProductBundleComponent {
    fun inject(singleProductBundleFragment: SingleProductBundleFragment)
}