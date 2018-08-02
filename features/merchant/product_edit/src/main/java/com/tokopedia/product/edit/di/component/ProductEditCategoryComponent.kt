package com.tokopedia.product.edit.di.component

import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.di.module.ProductEditCategoryModule
import com.tokopedia.product.edit.di.scope.ProductAddScope
import com.tokopedia.product.edit.price.ProductEditCategoryFragment
import dagger.Component

@ProductAddScope
@Component(modules = arrayOf(ProductEditCategoryModule::class), dependencies = arrayOf(ProductComponent::class))
public interface ProductEditCategoryComponent {
    fun inject(fragment: ProductEditCategoryFragment)
}