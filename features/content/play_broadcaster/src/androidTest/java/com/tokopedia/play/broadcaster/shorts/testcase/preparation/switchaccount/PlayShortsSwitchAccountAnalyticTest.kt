package com.tokopedia.play.broadcaster.shorts.testcase.preparation.switchaccount

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.shorts.builder.ShortsUiModelBuilder
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

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val cassavaValidator = PlayShortsCassavaValidator(cassavaTestRule)

    private val launcher = PlayShortsLauncher(targetContext)

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

    init {
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

        launcher.launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        }

        clickToolbar()

        cassavaValidator.verify("click - available account")
    }

    @Test
    fun testAnalytic_clickCloseSwitchAccount() {

        launcher.launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        }

        clickToolbar()
        clickCloseBottomSheet()

        cassavaValidator.verify("click - close switch profile")
    }

    @Test
    fun testAnalytic_clickUserAccount() {
        launcher.launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        }

        clickToolbar()
        clickUserAccount()

        cassavaValidator.verify("click - pilih akun user")
    }

    @Test
    fun testAnalytic_clickShopAccount() {
        launcher.launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        }

        clickToolbar()
        clickShopAccount()

        cassavaValidator.verify("click - pilih akun shop")
    }

    @Test
    fun testAnalytic_viewSwitchAccountToShopConfirmation() {

        launcher.launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
        }

        clickMenuTitle()
        inputTitle()
        submitTitle()

        clickToolbar()
        clickShopAccount()

        cassavaValidator.verify("view - switch profile shop bottom sheet")
    }

    @Test
    fun testAnalytic_clickCancelSwitchAccountToShop() {
        launcher.launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
        }

        clickMenuTitle()
        inputTitle()
        submitTitle()

        clickToolbar()
        clickShopAccount()
        clickCancelSwitchAccount()

        cassavaValidator.verify("click - batal switch to shop")
    }

    @Test
    fun testAnalytic_viewSwitchAccountToUserConfirmation() {
        launcher.launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        }

        clickMenuTitle()
        inputTitle()
        submitTitle()

        clickToolbar()
        clickUserAccount()

        cassavaValidator.verify("view - switch profile user bottom sheet")
    }

    @Test
    fun testAnalytic_clickCancelSwitchAccountToUser() {
        launcher.launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        }

        clickMenuTitle()
        inputTitle()
        submitTitle()

        clickToolbar()
        clickUserAccount()
        clickCancelSwitchAccount()

        cassavaValidator.verify("click - batal switch to user")
    }

    @Test
    fun testAnalytic_viewSwitchAccountBottomSheet() {
        launcher.launchActivity {
            coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        }

        clickToolbar()

        cassavaValidator.verify("view - switch profile bottom sheet")
    }
}
