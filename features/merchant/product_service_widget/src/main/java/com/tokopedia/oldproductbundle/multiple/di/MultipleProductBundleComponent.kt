package com.tokopedia.oldproductbundle.multiple.di

import com.tokopedia.oldproductbundle.common.di.ProductBundleComponent
import com.tokopedia.oldproductbundle.common.di.ProductBundleModule
import com.tokopedia.oldproductbundle.common.di.ProductBundleViewModelModule
import com.tokopedia.oldproductbundle.multiple.presentation.fragment.MultipleProductBundleFragment
import dagger.Component

@MultipleProductBundleScope
@Component(modules = [ProductBundleModule::class, ProductBundleViewModelModule::class],
    dependencies = [ProductBundleComponent::class])
interface MultipleProductBundleComponent {
    fun inject(fragment: MultipleProductBundleFragment)
}