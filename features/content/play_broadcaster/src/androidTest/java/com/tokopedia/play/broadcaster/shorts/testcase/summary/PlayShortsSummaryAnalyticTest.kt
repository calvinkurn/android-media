package com.tokopedia.play.broadcaster.shorts.testcase.summary

import android.content.Intent
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.helper.PlayBroadcastCassavaValidator
import com.tokopedia.play.broadcaster.shorts.builder.ShortsUiModelBuilder
import com.tokopedia.play.broadcaster.shorts.container.PlayShortsTestActivity
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsTestComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsTestModule
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.helper.*
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.compose.createAndroidIntentComposeRule
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
@CassavaTest
class PlayShortsSummaryAnalyticTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    @get:Rule
    val composeActivityTestRule = createAndroidIntentComposeRule<PlayShortsTestActivity> { context ->
        Intent(context, PlayShortsTestActivity::class.java)
    }

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val cassavaValidator = PlayBroadcastCassavaValidator.buildForShorts(cassavaTestRule)

    private val mockShortsRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockBroRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockProductTagRepo: ProductTagRepository = mockk(relaxed = true)
    private val mockUgcOnboardingRepo: UGCOnboardingRepository = mockk(relaxed = true)
    private val mockContentProductPickerSGCRepo: ContentProductPickerSellerRepository = mockk(relaxed = true)
    private val mockContentProductPickerSGCCommonRepo: ProductPickerSellerCommonRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockAccountManager: PlayShortsAccountManager = mockk(relaxed = true)
    private val mockCoachMarkSharedPref: ContentCoachMarkSharedPref = mockk(relaxed = true)

    private val uiModelBuilder = ShortsUiModelBuilder()

    private val mockShortsConfig = uiModelBuilder.buildShortsConfig()
    private val mockAccountList = uiModelBuilder.buildAccountListModel(usernameBuyer = false, tncBuyer = false)
    private val mockAccountShop = mockAccountList[0]
    private val mockProductTagSection = uiModelBuilder.buildProductTagSectionList()
    private val mockEtalaseProducts = uiModelBuilder.buildEtalaseProducts()
    private val mockTags = uiModelBuilder.buildTags()
    private val mockFirstTagText = mockTags.tags.first().tag
    private val mockException = Exception("Network Error")

    init {
        coEvery { mockCoachMarkSharedPref.hasBeenShown(any()) } returns true
        coEvery { mockCoachMarkSharedPref.hasBeenShown(any(), any()) } returns true
        coEvery { mockShortsRepo.getAccountList() } returns mockAccountList
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockAccountManager.isAllowChangeAccount(any()) } returns true
        coEvery { mockShortsRepo.getShortsConfiguration(any(), any()) } returns mockShortsConfig
        coEvery { mockUgcOnboardingRepo.validateUsername(any()) } returns Pair(true, "")
        coEvery { mockUgcOnboardingRepo.insertUsername(any()) } returns true
        coEvery { mockUgcOnboardingRepo.acceptTnc() } returns true
        coEvery { mockContentProductPickerSGCCommonRepo.getEtalaseList() } returns emptyList()
        coEvery { mockContentProductPickerSGCCommonRepo.getCampaignList() } returns emptyList()
        coEvery { mockContentProductPickerSGCRepo.getProductsInEtalase(any(), any(), any(), any()) } returns mockEtalaseProducts
        coEvery { mockContentProductPickerSGCRepo.setProductTags(any(), any()) } returns Unit
        coEvery { mockContentProductPickerSGCRepo.getProductTagSummarySection(any()) } returns mockProductTagSection
        coEvery { mockShortsRepo.getTagRecommendation(any()) } returns mockTags
        coEvery { mockShortsRepo.saveTag(any(), any()) } returns true
        coEvery { mockShortsRepo.updateStatus(any(), any(), any()) } throws mockException

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
                        mockContentProductPickerSGCCommonRepo = mockContentProductPickerSGCCommonRepo,
                        mockAccountManager = mockAccountManager,
                        mockUserSession = mockUserSession,
                        mockRouter = mockk(relaxed = true),
                        mockIdleManager = mockk(relaxed = true),
                        mockDataStore = mockk(relaxed = true),
                        mockCoachMarkSharedPref = mockCoachMarkSharedPref,
                    )
                )
                .build()
        )
    }

    private fun setupSummaryFlow(setupMock: () -> Unit) {
        setupMock()

        completeMandatoryMenu()

        clickContinueOnPreparationPage()
    }

    @Test
    fun testAnalytic_shorts_summaryPage() {
        setupSummaryFlow {
            var isError = true

            coEvery { mockShortsRepo.getTagRecommendation(any()) } coAnswers {
                if (isError) {
                    isError = false
                    throw mockException
                } else {
                    mockTags
                }
            }
        }

        composeActivityTestRule.clickRefreshContentTag()
        cassavaValidator.verify("click - refresh content tags")

        cassavaValidator.verifyOpenScreen("/play broadcast short - summary page - ${mockAccountShop.id} - seller")

        composeActivityTestRule.clickContentTag(mockFirstTagText)
        cassavaValidator.verify("click - content tag")

        clickUploadVideo()
        cassavaValidator.verify("click - upload video")

        clickBackOnSummaryPage()
        cassavaValidator.verify("click - back summary page")
    }
}
