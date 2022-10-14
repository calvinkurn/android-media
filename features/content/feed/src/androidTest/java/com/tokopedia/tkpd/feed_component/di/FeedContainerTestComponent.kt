package com.tokopedia.tkpd.feed_component.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.feedplus.view.di.FeedContainerComponent
import com.tokopedia.feedplus.view.di.FeedContainerScope
import com.tokopedia.feedplus.view.di.FeedContainerViewModelModule
import com.tokopedia.play.widget.di.PlayWidgetModule
import dagger.Component

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
@FeedContainerScope
@Component(
    modules = [
        FeedContainerTestModule::class,
        FeedContainerViewModelModule::class,
        PlayWidgetModule::class
    ], dependencies = [BaseAppComponent::class]
)
interface FeedContainerTestComponent : FeedContainerComponent
