package com.tokopedia.play.broadcaster.shorts.testcase.preparation.switchaccount

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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@CassavaTest
class PlayShortsSwitchAccountAnalyticTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

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
    private val mockAccountList = uiModelBuilder.buildAccountListModel()
    private val mockAccountShop = mockAccountList[0]
    private val mockAccountUser = mockAccountList[1]

    init {
        coEvery { mockCoachMarkSharedPref.hasBeenShown(any()) } returns true
        coEvery { mockCoachMarkSharedPref.hasBeenShown(any(), any()) } returns true
        coEvery { mockShortsRepo.getAccountList() } returns mockAccountList
        coEvery { mockShortsRepo.getShortsConfiguration(any(), any()) } returns mockShortsConfig
        coEvery { mockAccountManager.isAllowChangeAccount(any()) } returns true
        coEvery { mockShortsRepo.uploadTitle(any(), any(), any()) } returns Unit

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
                        mockContentProductPickerSGCRepo = mockContentProductPickerSGCRepo,
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

    @Test
    fun testAnalytic_shorts_switchAccount() {
        launcher.launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
            coEvery { mockAccountManager.switchAccount(any(), any()) } returns mockAccountUser
        }

        /** Setup title to display switch account confirmation */
        setupTitle()

        clickToolbar()
        cassavaValidator.verify("click - available account")
        cassavaValidator.verify("view - switch profile bottom sheet")

        clickCloseBottomSheet()
        cassavaValidator.verify("click - close switch profile")

        clickToolbar()
        clickShopAccount()
        cassavaValidator.verify("click - pilih akun shop")

        clickToolbar()
        clickUserAccount()
        cassavaValidator.verify("click - pilih akun user")
        cassavaValidator.verify("view - switch profile user bottom sheet")

        clickCancelSwitchAccount()
        cassavaValidator.verify("click - batal switch to user")

        /** Switch Account to User */
        clickToolbar()
        clickUserAccount()
        clickProceedSwitchAccount()

        /** Setup title to display switch account confirmation */
        setupTitle()

        clickToolbar()
        clickShopAccount()
        cassavaValidator.verify("view - switch profile shop bottom sheet")

        clickCancelSwitchAccount()
        cassavaValidator.verify("click - batal switch to shop")
    }

    private fun setupTitle() {
        clickMenuTitle()
        inputTitle()
        submitTitle()
    }
}
