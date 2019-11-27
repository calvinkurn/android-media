package com.tokopedia.events.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.events.di.scope.EventScope
import com.tokopedia.events.view.customview.SelectEventDateBottomSheet
import dagger.Component

@EventScope
@Component(modules = [EventViewModelModule::class, EventDependencyModule::class], dependencies = [BaseAppComponent::class])
interface EventViewModelComponent {

    fun inject(selectEventDateBottomSheet: SelectEventDateBottomSheet)
}