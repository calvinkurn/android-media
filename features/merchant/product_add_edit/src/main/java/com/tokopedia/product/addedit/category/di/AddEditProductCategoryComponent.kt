package com.tokopedia.product.addedit.category.di

import com.tokopedia.product.addedit.category.presentation.fragment.AddEditProductCategoryFragment
import com.tokopedia.product.addedit.common.di.AddEditProductComponent
import dagger.Component

@AddEditProductCategoryScope
@Component(modules = [AddEditProductCategoryModule::class], dependencies = [AddEditProductComponent::class])
interface AddEditProductCategoryComponent {
    fun inject(fragment: AddEditProductCategoryFragment)
}