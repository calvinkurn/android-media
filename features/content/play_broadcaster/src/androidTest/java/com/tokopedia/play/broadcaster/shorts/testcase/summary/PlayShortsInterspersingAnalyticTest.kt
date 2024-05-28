package com.tokopedia.play.broadcaster.shorts.testcase.summary

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.content.test.util.pressBack
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.helper.PlayBroadcastCassavaValidator
import com.tokopedia.play.broadcaster.shorts.builder.ShortsUiModelBuilder
import com.tokopedia.play.broadcaster.shorts.container.PlayShortsTestActivity
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsTestComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsTestModule
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.helper.PlayShortsInjector
import com.tokopedia.play.broadcaster.shorts.helper.clickBackInterspersingConfirmation
import com.tokopedia.play.broadcaster.shorts.helper.clickCloseBottomSheet
import com.tokopedia.play.broadcaster.shorts.helper.clickContentTag
import com.tokopedia.play.broadcaster.shorts.helper.clickContinueOnPreparationPage
import com.tokopedia.play.broadcaster.shorts.helper.clickInterspersingToggle
import com.tokopedia.play.broadcaster.shorts.helper.clickNextInterspersingConfirmation
import com.tokopedia.play.broadcaster.shorts.helper.clickUploadVideo
import com.tokopedia.play.broadcaster.shorts.helper.clickVideoPdpOnInterspersingConfirmation
import com.tokopedia.play.broadcaster.shorts.helper.completeMandatoryMenu
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.compose.createAndroidIntentComposeRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on January 16, 2024
 */
@CassavaTest
class PlayShortsInterspersingAnalyticTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    @get:Rule
    val composeActivityTestRule = createAndroidIntentComposeRule<PlayShortsTestActivity> { context ->
        Intent(context, PlayShortsTestActivity::class.java)
    }

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val cassavaValidator = PlayBroadcastCassavaValidator.buildForInterspersingVideo(cassavaTestRule)

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

    private val mockShortsConfig = uiModelBuilder.buildShortsConfig(eligibleInterspersing = true, productCountForInterspersing = 1)
    private val mockAccountList = uiModelBuilder.buildAccountListModel(usernameBuyer = false, tncBuyer = false)
    private val mockAccountShop = mockAccountList[0]
    private val mockProductTagSection = uiModelBuilder.buildProductTagSectionList(size = 1)
    private val mockEtalaseProducts = uiModelBuilder.buildEtalaseProducts()
    private val mockTags = uiModelBuilder.buildTags()
    private val mockFirstTagText = mockTags.tags.first().tag
    private val mockHasPdpVideo = uiModelBuilder.buildHasPdpVideo()
    private val mockHasNoPdpVideo = uiModelBuilder.buildHasNoPdpVideo()
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
        coEvery { mockShortsRepo.checkProductCustomVideo(any()) } returns mockHasPdpVideo
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

    private fun setupSummaryFlow() {
        completeMandatoryMenu()

        clickContinueOnPreparationPage()

        composeActivityTestRule.clickContentTag(mockFirstTagText)
    }

    @Test
    fun testAnalytic_interspersingVideo() {
        setupSummaryFlow()

        composeActivityTestRule.apply {
            clickInterspersingToggle()
            clickInterspersingToggle()
            verify("click - show video on pdp")

            coEvery { mockShortsRepo.checkProductCustomVideo(any()) } throws mockException
            clickUploadVideo()
            verify("view - error toaster show video PDP")

            coEvery { mockShortsRepo.checkProductCustomVideo(any()) } returns mockHasPdpVideo
            clickUploadVideo()
            clickCloseBottomSheet()
            verify("click - x icon video product option for pdp")

            clickUploadVideo()
            clickBackInterspersingConfirmation()
            verify("click - kembali show video on pdp")

            clickUploadVideo()
            clickVideoPdpOnInterspersingConfirmation()
            verify("click - video card option for pdp")

            pressBack()
            clickNextInterspersingConfirmation()
            verify("click - lanjut show video on pdp")
        }
    }

    private fun verify(eventAction: String) {
        cassavaValidator.verify(eventAction)
    }
}
