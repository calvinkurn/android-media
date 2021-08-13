package com.tokopedia.searchbar.navigation_component.di

import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.di.module.NavigationModule
import com.tokopedia.searchbar.navigation_component.di.module.NavigationViewModelModule
import dagger.Component

@NavigationScope
@Component(modules = [
    NavigationModule::class,
    NavigationViewModelModule::class
])
interface NavigationComponent {
    fun inject(view: NavToolbar)
}