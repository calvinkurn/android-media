package com.tokopedia.topads.auto.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topads.auto.di.module.AutoAdsModule
import com.tokopedia.topads.auto.di.module.AutoAdsQueryModule
import com.tokopedia.topads.auto.di.module.ViewModelModule
import com.tokopedia.topads.auto.view.fragment.*
import dagger.Component

/**
 * Author errysuprayogi on 16,May,2019
 */
@AutoAdsScope
@Component(modules = [AutoAdsModule::class, AutoAdsQueryModule::class, ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface AutoAdsComponent {

    fun inject(dailyBudgetFragment: AutoAdsBaseBudgetFragment)
    fun inject(createAutoAdsFragment: CreateAutoAdsFragment)
    fun inject(autoAdsOnboardingFragment: AutoAdsOnboardingFragScreen1)
    fun inject(autoAdsOnboardingFragment: AutoAdsOnboardingFragScreen2)
    fun inject(autoAdsOnboardingFragment: AutoAdsOnboardingFragScreen3)

}