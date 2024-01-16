package com.tokopedia.play.broadcaster.shorts.testcase.summary

import android.content.Intent
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
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
import com.tokopedia.play.broadcaster.shorts.helper.PlayShortsInjector
import com.tokopedia.play.broadcaster.shorts.helper.PlayShortsLauncher
import com.tokopedia.play.broadcaster.shorts.helper.clickContinueOnPreparationPage
import com.tokopedia.play.broadcaster.shorts.helper.completeMandatoryMenu
import com.tokopedia.play.broadcaster.shorts.view.activity.PlayShortsActivity
import com.tokopedia.test.application.compose.createAndroidIntentComposeRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on January 16, 2024
 */
class PlayShortsInterspersingAnalyticTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    @get:Rule
    val composeActivityTestRule = createAndroidIntentComposeRule<PlayShortsTestActivity> { context ->
        Intent(context, PlayShortsTestActivity::class.java)
    }

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val cassavaValidator = PlayBroadcastCassavaValidator.buildForShorts(cassavaTestRule)

    private val launcher = PlayShortsLauncher(targetContext)

    private val mockShortsRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockBroRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockProductTagRepo: ProductTagRepository = mockk(relaxed = true)
    private val mockUgcOnboardingRepo: UGCOnboardingRepository = mockk(relaxed = true)
    private val mockContentProductPickerSGCRepo: ContentProductPickerSellerRepository = mockk(relaxed = true)
    private val mockContentProductPickerSGCCommonRepo: ProductPickerSellerCommonRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockAccountManager: PlayShortsAccountManager = mockk(relaxed = true)

    private val uiModelBuilder = ShortsUiModelBuilder()

    private val mockShortsConfig = uiModelBuilder.buildShortsConfig()
    private val mockAccountList = uiModelBuilder.buildAccountListModel(usernameBuyer = false, tncBuyer = false)
    private val mockAccountShop = mockAccountList[0]
    private val mockProductTagSection = uiModelBuilder.buildProductTagSectionList(size = 1)
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
        coEvery { mockContentProductPickerSGCCommonRepo.getEtalaseList() } returns emptyList()
        coEvery { mockContentProductPickerSGCCommonRepo.getCampaignList() } returns emptyList()
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
                        mockContentProductPickerSGCCommonRepo = mockContentProductPickerSGCCommonRepo,
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

    private fun setupSummaryFlow() {
        completeMandatoryMenu()

        clickContinueOnPreparationPage()
    }

    @Test
    fun testAnalytic_interspersingVideo() {
        setupSummaryFlow()
    }
}
