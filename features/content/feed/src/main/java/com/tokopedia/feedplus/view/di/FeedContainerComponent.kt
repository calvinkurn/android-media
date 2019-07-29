package com.tokopedia.feedplus.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.feedplus.view.fragment.FeedPlusContainerFragment
import dagger.Component

@FeedContainerScope
@Component(modules = [FeedContainerModule::class, FeedContainerViewModelModule::class], dependencies = [BaseAppComponent::class])
interface FeedContainerComponent{

    fun inject(fragment: FeedPlusContainerFragment)
}