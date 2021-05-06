package com.tokopedia.product.addedit.specification.di

import com.tokopedia.product.addedit.common.di.AddEditProductComponent
import com.tokopedia.product.addedit.specification.presentation.fragment.AddEditProductSpecificationFragment
import dagger.Component

@AddEditProductSpecificationScope
@Component(modules = [AddEditProductSpecificationModule::class, AddEditProductSpecificationViewModelModule::class],
        dependencies = [AddEditProductComponent::class])
interface AddEditProductSpecificationComponent {
    fun inject(fragment: AddEditProductSpecificationFragment)
}