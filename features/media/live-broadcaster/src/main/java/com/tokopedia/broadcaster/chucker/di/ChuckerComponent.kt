package com.tokopedia.broadcaster.chucker.di

import com.tokopedia.broadcaster.chucker.di.module.ChuckerModule
import com.tokopedia.broadcaster.chucker.di.module.ChuckerViewModelModule
import com.tokopedia.broadcaster.chucker.di.scope.ChuckerScope
import com.tokopedia.broadcaster.chucker.ui.fragment.NetworkChuckerFragment
import dagger.Component

@ChuckerScope
@Component(modules = [
    ChuckerModule::class,
    ChuckerViewModelModule::class
])
interface ChuckerComponent {
    fun inject(fragment: NetworkChuckerFragment)
}