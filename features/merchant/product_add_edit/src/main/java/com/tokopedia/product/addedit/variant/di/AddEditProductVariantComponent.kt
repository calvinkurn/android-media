package com.tokopedia.product.addedit.variant.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.addedit.variant.presentation.fragment.AddEditProductVariantFragment
import dagger.Component

@AddEditProductVariantScope
@Component(modules = [AddEditProductVariantModule::class], dependencies = [BaseAppComponent::class])
interface AddEditProductVariantComponent {
    fun inject(fragment: AddEditProductVariantFragment)
}