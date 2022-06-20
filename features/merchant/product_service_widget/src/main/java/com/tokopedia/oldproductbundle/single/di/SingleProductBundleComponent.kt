package com.tokopedia.oldproductbundle.single.di

import com.tokopedia.oldproductbundle.common.di.ProductBundleComponent
import com.tokopedia.oldproductbundle.common.di.ProductBundleModule
import com.tokopedia.oldproductbundle.single.presentation.fragment.SingleProductBundleFragment
import dagger.Component

@SingleProductBundleScope
@Component(modules = [ProductBundleModule::class, SingleProductBundleViewModelModule::class],
    dependencies = [ProductBundleComponent::class])
interface SingleProductBundleComponent {
    fun inject(singleProductBundleFragment: SingleProductBundleFragment)
}