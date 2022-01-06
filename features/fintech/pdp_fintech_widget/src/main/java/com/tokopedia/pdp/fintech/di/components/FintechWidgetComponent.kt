package com.tokopedia.pdp.fintech.di.components

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.pdpsimulation.common.di.module.FintechWidgetModule
import com.tokopedia.pdpsimulation.common.di.module.ViewModelModule
import com.tokopedia.pdpsimulation.common.di.scope.FintechWidgetScope
import dagger.Component


@FintechWidgetScope
@Component(modules =
[FintechWidgetModule::class,
    ViewModelModule::class],
    dependencies = [BaseAppComponent::class])
interface FintechWidgetComponent {

}