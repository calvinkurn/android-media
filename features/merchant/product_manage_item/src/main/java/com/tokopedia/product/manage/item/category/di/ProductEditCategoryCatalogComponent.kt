package com.tokopedia.product.manage.item.di.component

import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.di.module.ProductEditCategoryCatalogModule
import com.tokopedia.product.manage.item.di.scope.ProductAddScope
import com.tokopedia.product.manage.item.price.ProductAddNameCategoryFragment
import com.tokopedia.product.manage.item.price.ProductEditCatalogPickerFragment
import com.tokopedia.product.manage.item.price.ProductEditCategoryFragment
import dagger.Component

@ProductAddScope
@Component(modules = arrayOf(ProductEditCategoryCatalogModule::class), dependencies = arrayOf(ProductComponent::class))
interface ProductEditCategoryCatalogComponent {
    fun inject(fragment: ProductEditCategoryFragment)
    fun inject(fragment: ProductEditCatalogPickerFragment)
    fun inject(fragment: ProductAddNameCategoryFragment)
}