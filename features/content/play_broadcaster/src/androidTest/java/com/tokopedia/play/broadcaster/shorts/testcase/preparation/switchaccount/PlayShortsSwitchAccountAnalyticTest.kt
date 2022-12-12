package com.tokopedia.play.broadcaster.shorts.testcase.preparation.switchaccount

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.helper.containsEventAction
import com.tokopedia.play.broadcaster.shorts.builder.ShortsUiModelBuilder
import com.tokopedia.play.broadcaster.shorts.const.DEFAULT_DELAY
import com.tokopedia.play.broadcaster.shorts.container.PlayShortsTestActivity
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsTestComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsTestModule
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.helper.PlayShortsInjector
import com.tokopedia.play.broadcaster.shorts.helper.*
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class PlayShortsSwitchAccountAnalyticTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val cassavaValidator = PlayShortsCassavaValidator(cassavaTestRule)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val mockShortsRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockBroRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockProductTagRepo: ProductTagRepository = mockk(relaxed = true)
    private val mockUgcOnboardingRepo: UGCOnboardingRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockAccountManager: PlayShortsAccountManager = mockk(relaxed = true)

    private val uiModelBuilder = ShortsUiModelBuilder()

    private val mockShortsConfig = uiModelBuilder.buildShortsConfig()
    private val mockAccountList = uiModelBuilder.buildAccountListModel()
    private val mockAccountShop = mockAccountList[0]
    private val mockAccountUser = mockAccountList[1]

    private fun launchActivity(initialMock: () -> Unit = {}) {
        initialMock()

        ActivityScenario.launch<PlayShortsTestActivity>(
            Intent(targetContext, PlayShortsTestActivity::class.java)
        )
    }

    init {
        coEvery { mockShortsRepo.getAccountList() } returns mockAccountList
        coEvery { mockShortsRepo.getShortsConfiguration(any(), any()) } returns mockShortsConfig
        coEvery { mockAccountManager.isAllowChangeAccount(any()) } returns true

        PlayShortsInjector.set(
            DaggerPlayShortsTestComponent.builder()
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playShortsTestModule(
                    PlayShortsTestModule(
                        activityContext = targetContext,
                        mockShortsRepo = mockShortsRepo,
                        mockBroRepo = mockBroRepo,
                        mockProductTagRepo = mockProductTagRepo,
                        mockUgcOnboardingRepo = mockUgcOnboardingRepo,
                        mockAccountManager = mockAccountManager,
                        mockUserSession = mockUserSession,
                        mockRouter = mockk(relaxed = true),
                        mockIdleManager = mockk(relaxed = true),
                    )
                )
                .build()
        )
    }

    @Test
    fun testAnalytic_clickSwitchAccount() {

        launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        }

        clickToolbar()

        cassavaValidator.verify("click - available account")
    }

    @Test
    fun testAnalytic_clickCloseSwitchAccount() {

        /** TODO: still failing */

        launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        }

        clickToolbar()
        closeSwitchAccountBottomSheet()

        cassavaValidator.verify("click - close switch profile")
    }

    @Test
    fun testAnalytic_clickUserAccount() {

    }

    @Test
    fun testAnalytic_clickShopAccount() {

    }

    @Test
    fun testAnalytic_viewSwitchAccountToShopConfirmation() {

    }

    @Test
    fun testAnalytic_clickCancelSwitchAccountToShop() {

    }

    @Test
    fun testAnalytic_viewSwitchAccountToUserConfirmation() {

    }

    @Test
    fun testAnalytic_clickCancelSwitchAccountToUser() {

    }
}
