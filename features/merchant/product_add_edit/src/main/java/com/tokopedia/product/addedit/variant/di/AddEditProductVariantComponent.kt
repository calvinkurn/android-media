package com.tokopedia.product.addedit.variant.di

import com.tokopedia.product.addedit.common.di.AddEditProductComponent
import com.tokopedia.product.addedit.variant.presentation.dialog.CustomVariantInputBottomSheet
import com.tokopedia.product.addedit.variant.presentation.dialog.CustomVariantManageBottomSheet
import com.tokopedia.product.addedit.variant.presentation.fragment.AddEditProductVariantDetailFragment
import com.tokopedia.product.addedit.variant.presentation.fragment.AddEditProductVariantFragment
import dagger.Component

@AddEditProductVariantScope
@Component(modules = [AddEditProductVariantModule::class], dependencies = [AddEditProductComponent::class])
interface AddEditProductVariantComponent {
    fun inject(fragment: AddEditProductVariantFragment)
    fun inject(fragment: AddEditProductVariantDetailFragment)
    fun inject(fragment: CustomVariantInputBottomSheet)
    fun inject(fragment: CustomVariantManageBottomSheet)
}