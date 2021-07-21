package com.tokopedia.entertainment.search.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.entertainment.search.fragment.EventCategoryFragment
import com.tokopedia.entertainment.search.fragment.EventSearchFragment
import dagger.Component

/**
 * Author errysuprayogi on 06,February,2020
 */
@EventSearchScope
@Component(modules = [EventSearchModule::class], dependencies = [BaseAppComponent::class])
interface EventSearchComponent {
    fun inject(eventSearchFragment: EventSearchFragment)
    fun inject(eventCategoryFragment: EventCategoryFragment)
}