package com.tokopedia.broadcaster.statsnerd.di

import com.tokopedia.broadcaster.statsnerd.di.module.ChuckerModule
import com.tokopedia.broadcaster.statsnerd.di.module.ChuckerViewModelModule
import com.tokopedia.broadcaster.statsnerd.di.scope.ChuckerScope
import com.tokopedia.broadcaster.statsnerd.ui.fragment.chucker.NetworkChuckerFragment
import dagger.Component

@ChuckerScope
@Component(modules = [
    ChuckerModule::class,
    ChuckerViewModelModule::class
])
interface ChuckerComponent {
    fun inject(fragment: NetworkChuckerFragment)
}