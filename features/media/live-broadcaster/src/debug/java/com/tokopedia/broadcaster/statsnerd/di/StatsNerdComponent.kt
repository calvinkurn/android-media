package com.tokopedia.broadcaster.statsnerd.di

import com.tokopedia.broadcaster.statsnerd.di.module.StatsNerdModule
import com.tokopedia.broadcaster.statsnerd.di.module.StatsNerdViewModelModule
import com.tokopedia.broadcaster.statsnerd.di.scope.StatsNerdScope
import com.tokopedia.broadcaster.statsnerd.ui.fragment.main.NetworkStatsNerdFragment
import dagger.Component

@StatsNerdScope
@Component(modules = [
    StatsNerdModule::class,
    StatsNerdViewModelModule::class
])
interface StatsNerdComponent {
    fun inject(fragment: NetworkStatsNerdFragment)
}