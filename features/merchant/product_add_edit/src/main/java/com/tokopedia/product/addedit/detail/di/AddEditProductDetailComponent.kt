package com.tokopedia.product.addedit.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.addedit.detail.presentation.fragment.AddEditProductDetailFragment
import dagger.Component

@AddEditProductDetailScope
@Component(modules = [AddEditProductDetailModule::class], dependencies = [BaseAppComponent::class])
interface AddEditProductDetailComponent {
    fun inject(fragment: AddEditProductDetailFragment)
}