package com.tokopedia.tkpd.feed_component.helper

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkManager
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.feedplus.view.di.FeedInjector
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tkpd.feed_component.di.DaggerFeedContainerTestComponent
import com.tokopedia.tkpd.feed_component.di.FeedContainerTestModule
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By : Jonathan Darwin on September 23, 2022
 */
class FeedContainerDaggerHelper(
    private val context: Context
) {

    fun setupDagger(
        mockUserSession: UserSessionInterface,
        mockRepo: FeedPlusRepository,
        mockRemoteConfig: RemoteConfig,
        mockContentCoachMarkManager: ContentCoachMarkManager,
    ) {
        FeedInjector.set(
            DaggerFeedContainerTestComponent.builder()
                .feedContainerTestModule(
                    FeedContainerTestModule(
                        context = context,
                        mockUserSession = mockUserSession,
                        mockRepo = mockRepo,
                        mockRemoteConfig = mockRemoteConfig,
                        mockContentCoachMarkManager = mockContentCoachMarkManager,
                    )
                )
                .baseAppComponent(
                    (context.applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
        )
    }
}
