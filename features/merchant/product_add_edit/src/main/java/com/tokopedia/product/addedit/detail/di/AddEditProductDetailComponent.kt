package com.tokopedia.product.addedit.detail.di

import com.tokopedia.product.addedit.common.di.AddEditProductComponent
import com.tokopedia.product.addedit.detail.presentation.fragment.AddEditProductDetailFragment
import dagger.Component

@AddEditProductDetailScope
@Component(modules = [AddEditProductDetailModule::class], dependencies = [AddEditProductComponent::class])
interface AddEditProductDetailComponent {
    fun inject(fragment: AddEditProductDetailFragment)
}