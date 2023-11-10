package com.tokopedia.feedplus.oldFeed.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.di.ContentCoachMarkSharedPrefModule
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponent
import com.tokopedia.feedplus.oldFeed.view.fragment.FeedPlusContainerFragment
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
        ContentCoachMarkSharedPrefModule::class,
    ], dependencies = [
        BaseAppComponent::class,
        CreationUploaderComponent::class,
    ]
)
interface FeedContainerComponent{

    fun inject(fragment: FeedPlusContainerFragment)

    fun userSessionInterface(): UserSessionInterface
}
