package com.tokopedia.product.manage.feature.filter.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterExpandChecklistFragment
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterExpandSelectFragment
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import dagger.Component

@ProductManageFilterScope
@Component(dependencies = [ProductManageComponent::class])
interface ProductManageFilterComponent {
    fun inject(view: ProductManageFilterFragment)
    fun inject(view: ProductManageFilterExpandChecklistFragment)
    fun inject(view: ProductManageFilterExpandSelectFragment)
}