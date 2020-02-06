package com.tokopedia.entertainment.home.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.entertainment.home.fragment.HomeEntertainmentFragment
import dagger.Component

/**
 * Author errysuprayogi on 06,February,2020
 */
@EntHomeScope
@Component(modules = [EntHomeModule::class], dependencies = [BaseAppComponent::class])
interface EntHomeComponent {
    fun inject(homeEntertainmentFragment: HomeEntertainmentFragment)
}