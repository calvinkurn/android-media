package com.tokopedia.product.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.detail.view.fragment.ProductDetailFragment
import dagger.Component

@ProductDetailScope
@Component(modules = [ProductDetailModule::class, ViewModelModule::class, GqlRawQueryModule::class],
        dependencies = [BaseAppComponent::class])
interface ProductDetailComponent{
    fun inject(fragment: ProductDetailFragment)
}