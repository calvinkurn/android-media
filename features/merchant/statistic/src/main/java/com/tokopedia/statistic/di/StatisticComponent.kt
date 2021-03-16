package com.tokopedia.statistic.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.statistic.di.module.StatisticModule
import com.tokopedia.statistic.di.module.StatisticUseCaseModule
import com.tokopedia.statistic.di.module.StatisticViewModelModule
import com.tokopedia.statistic.view.activity.StatisticActivity
import com.tokopedia.statistic.view.fragment.StatisticFragment
import dagger.Component

/**
 * Created By @ilhamsuaib on 09/06/20
 */

@StatisticScope
@Component(
        dependencies = [BaseAppComponent::class],
        modules = [
            StatisticModule::class, StatisticUseCaseModule::class,
            StatisticViewModelModule::class
        ]
)
interface StatisticComponent {

    fun inject(activity: StatisticActivity)

    fun inject(fragment: StatisticFragment)
}