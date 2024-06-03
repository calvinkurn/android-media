package com.tokopedia.play.broadcaster.shorts.testcase.preparation.ugconboarding

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.helper.PlayBroadcastCassavaValidator
import com.tokopedia.play.broadcaster.shorts.builder.ShortsUiModelBuilder
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsTestComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsTestModule
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.helper.*
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on December 13, 2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@CassavaTest
class PlayShortsUgcOnboardingAnalyticTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val cassavaValidator = PlayBroadcastCassavaValidator.buildForShorts(cassavaTestRule)

    private val launcher = PlayShortsLauncher(targetContext)

    private val mockShortsRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockBroRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockProductTagRepo: ProductTagRepository = mockk(relaxed = true)
    private val mockUgcOnboardingRepo: UGCOnboardingRepository = mockk(relaxed = true)
    private val mockContentProductPickerSGCRepo: ContentProductPickerSellerRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockAccountManager: PlayShortsAccountManager = mockk(relaxed = true)
    private val mockCoachMarkSharedPref: ContentCoachMarkSharedPref = mockk(relaxed = true)

    private val uiModelBuilder = ShortsUiModelBuilder()

    private val mockShortsConfig = uiModelBuilder.buildShortsConfig()
    private val mockAccountList = uiModelBuilder.buildAccountListModel(usernameBuyer = false, tncBuyer = false)
    private val mockAccountShop = mockAccountList[0]
    private val mockAccountUser = mockAccountList[1]

    init {
        coEvery { mockCoachMarkSharedPref.hasBeenShown(any()) } returns true
        coEvery { mockCoachMarkSharedPref.hasBeenShown(any(), any()) } returns true
        coEvery { mockShortsRepo.getAccountList() } returns mockAccountList
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockAccountManager.switchAccount(any(), any()) } returns mockAccountUser
        coEvery { mockAccountManager.isAllowChangeAccount(any()) } returns true
        coEvery { mockShortsRepo.getShortsConfiguration(any(), any()) } returns mockShortsConfig
        coEvery { mockUgcOnboardingRepo.validateUsername(any()) } returns Pair(true, "")
        coEvery { mockUgcOnboardingRepo.insertUsername(any()) } returns true
        coEvery { mockUgcOnboardingRepo.acceptTnc() } returns true

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
                        mockContentProductPickerSGCRepo = mockContentProductPickerSGCRepo,
                        mockAccountManager = mockAccountManager,
                        mockUserSession = mockUserSession,
                        mockContentProductPickerSGCCommonRepo = mockk(relaxed = true),
                        mockRouter = mockk(relaxed = true),
                        mockIdleManager = mockk(relaxed = true),
                        mockDataStore = mockk(relaxed = true),
                        mockCoachMarkSharedPref = mockCoachMarkSharedPref,
                    )
                )
                .build()
        )
    }

    @Before
    fun setUp() {
        launcher.launchActivity()

        openOnboardingUgcBottomSheet()
    }

    @Test
    fun testAnalytic_shorts_onboardingUGC() {
        cassavaValidator.verify("view - register user profile")

        clickCloseBottomSheet()
        cassavaValidator.verify("click - x register user profile")

        openOnboardingUgcBottomSheet()
        clickTextFieldUsername()
        inputUsername()
        cassavaValidator.verify("click - type user profile name")

        clickAcceptTnc()
        cassavaValidator.verify("click - accept t&c")

        clickSubmitUgcOnboarding()
        cassavaValidator.verify("click - lanjut register user profile")
    }

    private fun openOnboardingUgcBottomSheet() {
        clickToolbar()
        clickUserAccount()
    }
}
