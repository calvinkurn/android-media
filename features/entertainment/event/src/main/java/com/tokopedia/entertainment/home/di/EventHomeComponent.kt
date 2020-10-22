package com.tokopedia.entertainment.home.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.entertainment.home.fragment.EventHomeFragment
import dagger.Component

/**
 * Author errysuprayogi on 06,February,2020
 */
@EventHomeScope
@Component(modules = [EventHomeModule::class], dependencies = [BaseAppComponent::class])
interface EventHomeComponent {
    fun inject(eventHomeFragment: EventHomeFragment)
}