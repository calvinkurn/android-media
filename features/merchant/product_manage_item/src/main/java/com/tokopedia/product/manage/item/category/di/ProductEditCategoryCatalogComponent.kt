package com.tokopedia.product.manage.item.category.di

import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.main.add.view.fragment.ProductAddNameCategoryFragment
import com.tokopedia.product.manage.item.catalog.view.fragment.ProductEditCatalogPickerFragment
import com.tokopedia.product.manage.item.category.view.fragment.ProductEditCategoryFragment
import com.tokopedia.product.manage.item.main.add.di.ProductAddScope
import dagger.Component

@ProductAddScope
@Component(modules = arrayOf(ProductEditCategoryCatalogModule::class), dependencies = arrayOf(ProductComponent::class))
interface ProductEditCategoryCatalogComponent {
    fun inject(fragment: ProductEditCategoryFragment)
    fun inject(fragment: ProductEditCatalogPickerFragment)
    fun inject(fragment: ProductAddNameCategoryFragment)
}