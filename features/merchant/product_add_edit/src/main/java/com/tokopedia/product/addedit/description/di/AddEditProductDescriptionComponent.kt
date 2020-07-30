package com.tokopedia.product.addedit.description.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment
import dagger.Component

@AddEditProductDescriptionScope
@Component(modules = [AddEditProductDescriptionModule::class],
        dependencies = [BaseAppComponent::class])
interface AddEditProductDescriptionComponent {
    fun inject(fragment: AddEditProductDescriptionFragment)
}