package com.tokopedia.statistic.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.statistic.di.module.StatisticModule
import com.tokopedia.statistic.di.module.StatisticUseCaseModule
import com.tokopedia.statistic.di.module.StatisticViewModelModule
import com.tokopedia.statistic.presentation.view.fragment.StatisticFragment
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

    fun inject(fragment: StatisticFragment)
}