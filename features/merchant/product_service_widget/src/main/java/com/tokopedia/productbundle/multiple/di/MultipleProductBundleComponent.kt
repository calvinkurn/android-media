package com.tokopedia.productbundle.multiple.di

import com.tokopedia.productbundle.common.di.ProductBundleComponent
import com.tokopedia.productbundle.common.di.ProductBundleModule
import com.tokopedia.productbundle.common.di.ProductBundleViewModelModule
import com.tokopedia.productbundle.multiple.presentation.fragment.MultipleProductBundleFragment
import dagger.Component

@MultipleProductBundleScope
@Component(modules = [ProductBundleModule::class, ProductBundleViewModelModule::class],
    dependencies = [ProductBundleComponent::class])
interface MultipleProductBundleComponent {
    fun inject(fragment: MultipleProductBundleFragment)
}