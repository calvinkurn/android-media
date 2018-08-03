package com.tokopedia.product.edit.di.component

import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.di.module.ProductEditCategoryCatalogModule
import com.tokopedia.product.edit.di.scope.ProductAddScope
import com.tokopedia.product.edit.price.ProductEditCatalogPickerFragment
import com.tokopedia.product.edit.price.ProductEditCategoryFragment
import dagger.Component

@ProductAddScope
@Component(modules = arrayOf(ProductEditCategoryCatalogModule::class), dependencies = arrayOf(ProductComponent::class))
interface ProductEditCategoryCatalogComponent {
    fun inject(fragment: ProductEditCategoryFragment)
    fun inject(fragment: ProductEditCatalogPickerFragment)
}