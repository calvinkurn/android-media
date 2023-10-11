package com.tokopedia.play.broadcaster.shorts.testcase.summary

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.product.picker.seller.domain.ContentProductPickerSGCRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.helper.PlayBroadcastCassavaValidator
import com.tokopedia.play.broadcaster.shorts.builder.ShortsUiModelBuilder
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsTestComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsTestModule
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.helper.*
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on December 15, 2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class PlayShortsSummaryAnalyticTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val cassavaValidator = PlayBroadcastCassavaValidator.buildForShorts(cassavaTestRule)

    private val launcher = PlayShortsLauncher(targetContext)

    private val mockShortsRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockBroRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockProductTagRepo: ProductTagRepository = mockk(relaxed = true)
    private val mockUgcOnboardingRepo: UGCOnboardingRepository = mockk(relaxed = true)
    private val mockContentProductPickerSGCRepo: ContentProductPickerSGCRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockAccountManager: PlayShortsAccountManager = mockk(relaxed = true)

    private val uiModelBuilder = ShortsUiModelBuilder()

    private val mockShortsConfig = uiModelBuilder.buildShortsConfig()
    private val mockAccountList = uiModelBuilder.buildAccountListModel(usernameBuyer = false, tncBuyer = false)
    private val mockAccountShop = mockAccountList[0]
    private val mockProductTagSection = uiModelBuilder.buildProductTagSectionList()
    private val mockEtalaseProducts = uiModelBuilder.buildEtalaseProducts()
    private val mockTags = uiModelBuilder.buildTags()
    private val mockException = Exception("Network Error")

    init {
        coEvery { mockShortsRepo.getAccountList() } returns mockAccountList
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockAccountManager.isAllowChangeAccount(any()) } returns true
        coEvery { mockShortsRepo.getShortsConfiguration(any(), any()) } returns mockShortsConfig
        coEvery { mockUgcOnboardingRepo.validateUsername(any()) } returns Pair(true, "")
        coEvery { mockUgcOnboardingRepo.insertUsername(any()) } returns true
        coEvery { mockUgcOnboardingRepo.acceptTnc() } returns true
        coEvery { mockContentProductPickerSGCRepo.getEtalaseList() } returns emptyList()
        coEvery { mockContentProductPickerSGCRepo.getCampaignList() } returns emptyList()
        coEvery { mockContentProductPickerSGCRepo.getProductsInEtalase(any(), any(), any(), any()) } returns mockEtalaseProducts
        coEvery { mockContentProductPickerSGCRepo.setProductTags(any(), any()) } returns Unit
        coEvery { mockContentProductPickerSGCRepo.getProductTagSummarySection(any()) } returns mockProductTagSection
        coEvery { mockShortsRepo.getTagRecommendation(any()) } returns mockTags
        coEvery { mockShortsRepo.saveTag(any(), any()) } returns true

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
                        mockRouter = mockk(relaxed = true),
                        mockIdleManager = mockk(relaxed = true),
                        mockDataStore = mockk(relaxed = true),
                    )
                )
                .build()
        )
    }

    private fun setupSummaryFlow(setupMock: () -> Unit) {
        launcher.launchActivity {
            setupMock()
        }

        completeMandatoryMenu()

        clickContinueOnPreparationPage()
    }

    @Test
    fun testAnalytic_clickBackOnSummaryPage() {
        setupSummaryFlow {
            coEvery { mockShortsRepo.getTagRecommendation(any()) } returns mockTags
        }

        clickBackOnSummaryPage()

        cassavaValidator.verify("click - back summary page")
    }

    @Test
    fun testAnalytic_clickContentTag() {
        setupSummaryFlow {
            coEvery { mockShortsRepo.getTagRecommendation(any()) } returns mockTags
        }

        clickContentTag()

        cassavaValidator.verify("click - content tag")
    }

    @Test
    fun testAnalytic_clickUploadVideo() {
        setupSummaryFlow {
            coEvery { mockShortsRepo.getTagRecommendation(any()) } returns mockTags
        }

        clickContentTag()
        clickUploadVideo()

        cassavaValidator.verify("click - upload video")
    }

    @Test
    fun testAnalytic_openScreenSummaryPage() {
        setupSummaryFlow {
            coEvery { mockShortsRepo.getTagRecommendation(any()) } returns mockTags
        }

        cassavaValidator.verifyOpenScreen("/play broadcast short - summary page - ${mockAccountShop.id} - seller")
    }

    @Test
    fun testAnalytic_clickRefreshContentTag() {
        setupSummaryFlow {
            coEvery { mockShortsRepo.getTagRecommendation(any()) } throws mockException
        }

        clickRefreshContentTag()

        cassavaValidator.verify("click - refresh content tags")
    }
}
