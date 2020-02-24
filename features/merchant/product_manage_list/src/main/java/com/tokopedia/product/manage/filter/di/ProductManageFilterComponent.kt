package com.tokopedia.product.manage.filter.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.common.di.ProductManageModule
import com.tokopedia.product.manage.filter.presentation.fragment.ProductManageFilterFragment
import dagger.Component

@ProductManageFilterScope
@Component(modules = [ProductManageFilterModule::class], dependencies = [ProductManageComponent::class])
interface ProductManageFilterComponent {
    fun inject(view: ProductManageFilterFragment)
}