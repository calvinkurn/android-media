package com.tokopedia.addon.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.addon.presentation.customview.AddOnWidgetView
import dagger.Component

@AddOnScope
@Component(modules = [AddOnModule::class, AddOnViewModelModule::class], dependencies = [BaseAppComponent::class])
interface AddOnComponent {
    fun inject(addOnWidgetView: AddOnWidgetView)
}
