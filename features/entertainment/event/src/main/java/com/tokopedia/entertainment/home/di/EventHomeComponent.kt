package com.tokopedia.entertainment.home.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.entertainment.home.fragment.NavEventHomeFragment
import dagger.Component

/**
 * Author errysuprayogi on 06,February,2020
 */
@EventHomeScope
@Component(modules = [EventHomeModule::class, EventHomeViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface EventHomeComponent {
    fun inject(navEventHomeFragment: NavEventHomeFragment)
}