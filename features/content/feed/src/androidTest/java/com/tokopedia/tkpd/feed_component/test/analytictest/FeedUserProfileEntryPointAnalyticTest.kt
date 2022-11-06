package com.tokopedia.tkpd.feed_component.test.analytictest

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.tkpd.feed_component.container.FeedContainerTestActivity
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.tkpd.feed_component.helper.select
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.tkpd.feed_component.builder.DynamicTabsModelBuilder
import com.tokopedia.tkpd.feed_component.builder.WhitelistModelBuilder
import com.tokopedia.tkpd.feed_component.const.DEFAULT_DELAY
import com.tokopedia.tkpd.feed_component.const.INITIAL_DELAY
import com.tokopedia.tkpd.feed_component.helper.FeedContainerDaggerHelper
import com.tokopedia.tkpd.feed_component.helper.FeedCassavaHelper
import com.tokopedia.tkpd.feed_component.helper.clickView
import com.tokopedia.tkpd.feed_component.helper.delay
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

    /** Helper */
    private val cassavaHelper = FeedCassavaHelper("tracker/content/feed/feed_page.json", cassavaTestRule)
    private val daggerHelper = FeedContainerDaggerHelper(targetContext)

    /** Builder */
    private val whitelistModelBuilder = WhitelistModelBuilder()
    private val dynamicTabModelBuilder = DynamicTabsModelBuilder()

    /** Response */
    private var whitelistResponse = whitelistModelBuilder.buildUgcOnly()
    private var dynamicTabsResponse = dynamicTabModelBuilder.buildFeedExploreTab()

    /** Mock */
    private val mockRepo = mockk<FeedPlusRepository>(relaxed = true)
    private val mockUserSession = mockk<UserSessionInterface>(relaxed = true)

    init {
        coEvery { mockRepo.getWhitelist() } returns whitelistResponse
        coEvery { mockRepo.getDynamicTabs() } returns dynamicTabsResponse

        coEvery { mockUserSession.userId } returns "203620293"
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.hasShop() } returns false
        coEvery { mockUserSession.isAffiliate } returns false

        daggerHelper.setupDagger(
            mockUserSession = mockUserSession,
            mockRepo = mockRepo
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
}
