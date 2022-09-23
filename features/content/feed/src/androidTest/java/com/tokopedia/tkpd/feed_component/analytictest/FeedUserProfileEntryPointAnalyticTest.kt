package com.tokopedia.tkpd.feed_component.analytictest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.tkpd.feed_component.container.FeedContainerTestActivity
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import com.tokopedia.tkpd.feed_component.fake.FakeFeedPlusRepository
import com.tokopedia.tkpd.feed_component.robot.FeedPlusContainerRobot
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
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

    private val analyticFile = "tracker/content/feed/feed_page.json"

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val robot = FeedPlusContainerRobot()
    private val mockRepo: FeedPlusRepository = FakeFeedPlusRepository()
//    private val mockUserSession: UserSessionInterface = UserSession(targetContext)
//    private val mockRepo = mockk<FeedPlusRepository>(relaxed = true)
    private val mockUserSession = mockk<UserSessionInterface>(relaxed = true)

    init {
//        mockUserSession.setIsLogin(true)

//        coEvery { mockRepo.getWhitelist() } returns WhitelistQuery()
        coEvery { mockUserSession.isLoggedIn } returns true

        robot.setUpDagger(
            mockUserSession = mockUserSession,
            mockRepo = mockRepo
        )
    }

//    @Before
//    fun setUp() {
////        coEvery { mockRepo.getWhitelist() } returns WhitelistQuery()
//
//        mockUserSession.setIsLogin(true)
//
//        robot.setUpDagger(
//            mockUserSession = mockUserSession,
//            mockRepo = mockRepo
//        )
////        robot.setUpDagger(
////            mockUserSession = mockUserSession,
////            mockRepo = mockRepo
////        )
//    }

    @Test
    fun onClick_user_profile_icon() {
        Thread.sleep(3000)
    }
}
