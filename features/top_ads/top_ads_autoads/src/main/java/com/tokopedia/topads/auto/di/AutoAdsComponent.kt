package com.tokopedia.topads.auto.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topads.auto.di.module.AutoAdsModule
import com.tokopedia.topads.auto.di.module.AutoAdsQueryModule
import com.tokopedia.topads.auto.di.module.ViewModelModule
import com.tokopedia.topads.auto.view.activity.AutoAdsRouteActivity
import com.tokopedia.topads.auto.view.fragment.DailyBudgetFragment
import com.tokopedia.topads.auto.view.widget.AutoAdsWidgetView
import dagger.Component

/**
 * Author errysuprayogi on 16,May,2019
 */
@AutoAdsScope
@Component(modules = [AutoAdsModule::class, AutoAdsQueryModule::class, ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface AutoAdsComponent {

    fun inject(adsRouteActivity: AutoAdsRouteActivity)

    fun inject(dailyBudgetFragment: DailyBudgetFragment)

    fun inject(autoAdsWidgetView: AutoAdsWidgetView)

}