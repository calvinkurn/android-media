package com.tokopedia.tkpd.feed_component.analytictest

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.tkpd.feed_component.container.FeedContainerTestActivity
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.tkpd.feed_component.util.select
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.feedplus.view.di.FeedInjector
import com.tokopedia.tkpd.feed_component.di.DaggerFeedContainerTestComponent
import com.tokopedia.tkpd.feed_component.di.FeedContainerTestModule
import com.tokopedia.tkpd.feed_component.util.FeedCassavaHelper
import com.tokopedia.tkpd.feed_component.util.clickView
import com.tokopedia.tkpd.feed_component.util.delay
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class FeedUserProfileEntryPointAnalyticTest {

    @get:Rule
    var activityRule = object : ActivityTestRule<FeedContainerTestActivity>(FeedContainerTestActivity::class.java) {
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val cassavaHelper = FeedCassavaHelper("tracker/content/feed/feed_page.json", cassavaTestRule)

    private var whitelistResponse = WhitelistQuery(
        whitelist = Whitelist(
            authors = listOf(
                Author(
                    type = "content-user",
                    thumbnail = "https://images.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-24.jpg"
                )
            )
        )
    )

    private var dynamicTabsResponse: FeedTabs = FeedTabs(
        feedData = listOf(
            FeedTabs.FeedData(
                isActive = true,
                key = "feeds",
                type = "feeds",
                position = 1,
                title = "Update"
            ),
            FeedTabs.FeedData(
                isActive = true,
                key = "explore",
                type = "explore",
                position = 2,
                title = "Explore"
            )
        )
    )

    private val mockRepo = mockk<FeedPlusRepository>()
    private val mockUserSession = mockk<UserSessionInterface>()

    init {
        coEvery { mockRepo.getWhitelist() } returns whitelistResponse
        coEvery { mockRepo.getDynamicTabs() } returns dynamicTabsResponse

        coEvery { mockUserSession.userId } returns "203620293"
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.hasShop() } returns false
        coEvery { mockUserSession.isAffiliate } returns false

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

    @Test
    fun onClick_user_profile_icon() {
        delay(INITIAL_DELAY)

        select(R.id.iv_feed_user)
            .clickView()

        delay(DEFAULT_DELAY)

        cassavaHelper.assertCassavaByEventAction("click - user profile icon")
    }
    companion object {
        private const val INITIAL_DELAY = 3000L
        private const val DEFAULT_DELAY = 1000L
    }
}
