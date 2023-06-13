package com.tokopedia.tkpd.feed_component.test.uitest

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkManager
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tkpd.feed_component.builder.DynamicTabsModelBuilder
import com.tokopedia.tkpd.feed_component.builder.WhitelistModelBuilder
import com.tokopedia.tkpd.feed_component.const.INITIAL_DELAY
import com.tokopedia.tkpd.feed_component.container.FeedContainerTestActivity
import com.tokopedia.tkpd.feed_component.helper.*
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on September 23, 2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class FeedUserProfileEntryPointUiTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    /** Helper */
    private val daggerHelper = FeedContainerDaggerHelper(targetContext)

    /** Builder */
    private val whitelistModelBuilder = WhitelistModelBuilder()
    private val dynamicTabModelBuilder = DynamicTabsModelBuilder()

    /** Response */
    private var whitelistUgcOnly = whitelistModelBuilder.buildUgcOnly()
    private var whitelistSellerOnly = whitelistModelBuilder.buildSellerOnly()
    private var whitelistComplete = whitelistModelBuilder.buildComplete()
    private var dynamicTabsResponse = dynamicTabModelBuilder.buildFeedExploreTab()

    /** Mock */
    private val mockRepo = mockk<FeedPlusRepository>(relaxed = true)
    private val mockUserSession = mockk<UserSessionInterface>(relaxed = true)
    private val mockRemoteConfig = mockk<RemoteConfig>(relaxed = true)
    private val mockContentCoachMarkManager = mockk<ContentCoachMarkManager>(relaxed = true)

    init {
        coEvery { mockRepo.getDynamicTabs() } returns dynamicTabsResponse
        coEvery { mockUserSession.isAffiliate } returns false

        daggerHelper.setupDagger(
            mockUserSession = mockUserSession,
            mockRepo = mockRepo,
            mockRemoteConfig = mockRemoteConfig,
            mockContentCoachMarkManager = mockContentCoachMarkManager,
        )
    }

    private fun launchActivity() {
        ActivityScenario.launch<FeedContainerTestActivity>(
            Intent(targetContext, FeedContainerTestActivity::class.java)
        )
    }

    @Test
    fun userProfileEntryPoint_notLoggedIn_entryPointHidden() {
        coEvery { mockUserSession.isLoggedIn } returns false
        coEvery { mockUserSession.userId } returns ""
        coEvery { mockUserSession.hasShop() } returns false

        launchActivity()

        delay(INITIAL_DELAY)

        select(R.id.iv_feed_user)
            .isHidden()
    }

    @Test
    fun userProfileEntryPoint_loggedIn_seller_entryPointHidden() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns "203620293"
        coEvery { mockUserSession.hasShop() } returns true

        coEvery { mockRepo.getWhitelist() } returns whitelistSellerOnly

        launchActivity()

        delay(INITIAL_DELAY)

        select(R.id.iv_feed_user)
            .isHidden()
    }

    @Test
    fun userProfileEntryPoint_loggedIn_ugc_entryPointShow() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns "203620293"
        coEvery { mockUserSession.hasShop() } returns false

        coEvery { mockRepo.getWhitelist() } returns whitelistUgcOnly

        launchActivity()

        delay(INITIAL_DELAY)

        select(R.id.iv_feed_user)
            .isVisible()
    }

    @Test
    fun userProfileEntryPoint_loggedIn_sellerAndUgc_entryPointShow() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns "203620293"
        coEvery { mockUserSession.hasShop() } returns true

        coEvery { mockRepo.getWhitelist() } returns whitelistComplete

        launchActivity()

        delay(INITIAL_DELAY)

        select(R.id.iv_feed_user)
            .isVisible()
    }
}
