package com.tokopedia.play.broadcaster.shorts.testcase.preparation

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.shorts.builder.ShortsUiModelBuilder
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsTestComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsTestModule
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.helper.*
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
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
class PlayShortsPreparationAnalyticTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

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
    private val mockAccountList = uiModelBuilder.buildAccountListModel(usernameBuyer = false, tncBuyer = false)
    private val mockAccountShop = mockAccountList[0]
    private val mockProductTagSection = uiModelBuilder.buildProductTagSectionList()
    private val mockEtalaseProducts = uiModelBuilder.buildEtalaseProducts()

    init {
        coEvery { mockShortsRepo.getAccountList() } returns mockAccountList
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockAccountManager.isAllowChangeAccount(any()) } returns true
        coEvery { mockShortsRepo.getShortsConfiguration(any(), any()) } returns mockShortsConfig
        coEvery { mockBroRepo.getEtalaseList() } returns emptyList()
        coEvery { mockBroRepo.getCampaignList() } returns emptyList()
        coEvery { mockBroRepo.getProductsInEtalase(any(), any(), any(), any()) } returns mockEtalaseProducts
        coEvery { mockBroRepo.setProductTags(any(), any()) } returns Unit
        coEvery { mockBroRepo.getProductTagSummarySection(any()) } returns mockProductTagSection

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

    @Before
    fun setUp() {
        launcher.launchActivity()
    }

    @Test
    fun testAnalytic_viewPreparationPage() {
        cassavaValidator.verify("view - post creation page")
    }

    @Test
    fun testAnalytic_clickMenuTitle() {
        clickMenuTitle()

        cassavaValidator.verify("click - edit title")
    }

    @Test
    fun testAnalytic_clickMenuProduct() {
        clickMenuProduct()

        cassavaValidator.verify("click - add product tag")
    }

    @Test
    fun testAnalytic_clickMenuCover() {

        completeMandatoryMenu()

        clickMenuCover()

        cassavaValidator.verify("click - add cover on preparation page")
    }

    @Test
    fun testAnalytic_viewLeavePreparationConfirmationPopup() {
        clickClosePreparationPage()

        cassavaValidator.verify("view - yakin mau keluar botom sheet")
    }

    @Test
    fun testAnalytic_clickContinueOnLeaveConfirmationPopup() {
        clickClosePreparationPage()
        clickContinueOnLeaveConfirmationPopup()

        cassavaValidator.verify("click - lanjut persiapan bottom sheet")
    }

    @Test
    fun testAnalytic_clickNextOnPreparationPage() {
        completeMandatoryMenu()
        clickContinueOnPreparationPage()

        cassavaValidator.verify("click - lanjut post creation")
    }
}
