package com.tokopedia.tkpd.feed_component.analytictest

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.feedplus.view.di.FeedInjector
import com.tokopedia.tkpd.feed_component.container.FeedContainerTestActivity
import com.tokopedia.tkpd.feed_component.di.DaggerFeedContainerTestComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
class FeedUserProfileEntryPointAnalyticTest {

    @get:Rule
    var activityRule = object : ActivityTestRule<FeedContainerTestActivity>(
        FeedContainerTestActivity::class.java) {

    }

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        FeedInjector.set(
            DaggerFeedContainerTestComponent.builder()
                .baseAppComponent(
                    (targetContext.applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
        )
    }

    @Test
    fun onClick_user_profile_icon() {
        Thread.sleep(3000)
    }
}
