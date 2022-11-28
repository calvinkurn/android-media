package com.tokopedia.feedplus.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.uploader.di.PlayShortsUploaderModule
import com.tokopedia.feedplus.view.fragment.FeedPlusContainerFragment
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@FeedContainerScope
@Component(
    modules = [
        FeedContainerModule::class,
        FeedContainerBindModule::class,
        FeedContainerViewModelModule::class,
        PlayWidgetModule::class,
        PlayShortsUploaderModule::class
    ], dependencies = [BaseAppComponent::class])
interface FeedContainerComponent{

    fun inject(fragment: FeedPlusContainerFragment)

    fun userSessionInterface(): UserSessionInterface
}
