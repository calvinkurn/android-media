package com.tokopedia.tkpd.feed_component.robot

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.feedplus.view.di.FeedInjector
import com.tokopedia.tkpd.feed_component.di.DaggerFeedContainerTestComponent
import com.tokopedia.tkpd.feed_component.di.FeedContainerTestModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By : Jonathan Darwin on September 23, 2022
 */
class FeedPlusContainerRobot {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    fun setUpDagger(
        mockUserSession: UserSessionInterface = UserSession(targetContext),
        mockRepo: FeedPlusRepository
    ) {
        FeedInjector.set(
            DaggerFeedContainerTestComponent.builder()
                .feedContainerTestModule(
                    FeedContainerTestModule(
                        mockUserSession = mockUserSession,
                        mockRepo = mockRepo
                    )
                )
                .baseAppComponent(
                    (targetContext.applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
        )
    }
}
