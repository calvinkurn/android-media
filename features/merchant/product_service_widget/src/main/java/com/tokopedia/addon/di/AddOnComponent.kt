package com.tokopedia.addon.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.addon.presentation.bottomsheet.AddOnBottomSheet
import com.tokopedia.unifycomponents.BaseCustomView
import dagger.Component

@AddOnScope
@Component(modules = [AddOnModule::class, AddOnViewModelModule::class], dependencies = [BaseAppComponent::class])
interface AddOnComponent {
    fun inject(bottomSheet: AddOnBottomSheet)
    fun inject(customView: BaseCustomView)
}
