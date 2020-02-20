package com.tokopedia.topads.widget.dashboard.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topads.widget.dashboard.view.DashboardWidgetFragment
import dagger.Component

/**
 * Author errysuprayogi on 25,October,2019
 */

@DashboardWidgetScope
@Component(modules = [DashboardWidgetModule::class, ViewModelModule::class, QueryModule::class], dependencies = [BaseAppComponent::class])
interface DashboardWidgetComponent {
    fun inject(dashboardWidgetFragment: DashboardWidgetFragment)
}