package com.tokopedia.product.addedit.draft.di

import com.tokopedia.product.addedit.common.di.AddEditProductComponent
import com.tokopedia.product.addedit.detail.presentation.fragment.AddEditProductDetailFragment
import com.tokopedia.product.addedit.draft.presentation.fragment.AddEditProductDraftFragment
import dagger.Component

@AddEditProductDraftScope
@Component(modules = [AddEditProductDraftModule::class], dependencies = [AddEditProductComponent::class])
interface AddEditProductDraftComponent {
    fun inject(fragment: AddEditProductDraftFragment)
}