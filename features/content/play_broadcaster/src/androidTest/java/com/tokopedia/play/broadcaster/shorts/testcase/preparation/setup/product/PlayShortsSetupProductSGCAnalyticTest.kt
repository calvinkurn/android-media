package com.tokopedia.play.broadcaster.shorts.testcase.preparation.setup.product

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.helper.PlayBroadcastCassavaValidator
import com.tokopedia.play.broadcaster.shorts.builder.ShortsUiModelBuilder
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsTestComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsTestModule
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.helper.*
import com.tokopedia.play.broadcaster.shorts.view.manager.idle.PlayShortsIdleManager
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on December 15, 2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@CassavaTest
class PlayShortsSetupProductSGCAnalyticTest {

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
    private val mockContentProductPickerSGCCommonRepo: ProductPickerSellerCommonRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockAccountManager: PlayShortsAccountManager = mockk(relaxed = true)
    private val mockIdleManager: PlayShortsIdleManager = mockk(relaxed = true)
    private val mockCoachMarkSharedPref: ContentCoachMarkSharedPref = mockk(relaxed = true)

    private val uiModelBuilder = ShortsUiModelBuilder()

    private val mockShortsConfig = uiModelBuilder.buildShortsConfig()
    private val mockAccountList = uiModelBuilder.buildAccountListModel(usernameBuyer = false, tncBuyer = false)
    private val mockAccountShop = mockAccountList[0]
    private val mockEtalaseList = uiModelBuilder.buildEtalaseList()
    private val mockCampaignList = uiModelBuilder.buildCampaignList()
    private val mockProductTagSection = uiModelBuilder.buildProductTagSectionList()
    private val mockEtalaseProducts = uiModelBuilder.buildEtalaseProducts()

    init {
        coEvery { mockCoachMarkSharedPref.hasBeenShown(any()) } returns true
        coEvery { mockCoachMarkSharedPref.hasBeenShown(any(), any()) } returns true
        coEvery { mockShortsRepo.getAccountList() } returns mockAccountList
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockAccountManager.isAllowChangeAccount(any()) } returns true
        coEvery { mockShortsRepo.getShortsConfiguration(any(), any()) } returns mockShortsConfig
        coEvery { mockContentProductPickerSGCCommonRepo.getEtalaseList() } returns mockEtalaseList
        coEvery { mockContentProductPickerSGCCommonRepo.getCampaignList() } returns mockCampaignList
        coEvery { mockContentProductPickerSGCRepo.getProductsInEtalase(any(), any(), any(), any()) } returns mockEtalaseProducts
        coEvery { mockContentProductPickerSGCRepo.setProductTags(any(), any()) } returns Unit
        coEvery { mockContentProductPickerSGCRepo.getProductTagSummarySection(any()) } returns mockProductTagSection
        coEvery { mockIdleManager.state } returns MutableSharedFlow()
        coEvery { mockIdleManager.startIdleTimer(any()) } returns Unit
        coEvery { mockIdleManager.clear() } returns Unit
        coEvery { mockIdleManager.forceStandByMode() } returns Unit
        coEvery { mockIdleManager.toggleState(any()) } returns Unit

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
                        mockIdleManager = mockIdleManager,
                        mockRouter = mockk(relaxed = true),
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

        clickMenuProduct()
        delay()
    }

    @Test
    fun testAnalytic_shorts_productSetupSgc() {
        cassavaValidator.verify("view - product selection bottom sheet")

        clickCloseBottomSheet()
        delay()
        cassavaValidator.verify("click - close button on product bottom sheet")

        clickMenuProduct()
        delay()
        clickSearchBarProductPickerSGC()
        delay()
        cassavaValidator.verify("click - search product")

        clickSortChip()
        delay()
        cassavaValidator.verify("view - sorting bottom sheet")
        cassavaValidator.verify("click - product sort")

        selectSortType()
        delay()
        clickSaveSort()
        delay()
        cassavaValidator.verify("click - sort type")

        clickSortChip()
        delay()
        clickCloseBottomSheet()
        delay()
        cassavaValidator.verify("click - close sort product")

        clickEtalaseAndCampaignChip()
        delay()
        cassavaValidator.verify("view - filter bottom sheet")
        cassavaValidator.verify("click - filter product etalase")

        clickCloseBottomSheet()
        delay()
        cassavaValidator.verify("click - close filter bottom sheet")

        clickEtalaseAndCampaignChip()
        delay()
        selectCampaign()
        delay()
        cassavaValidator.verify("click - campaign card")

        clickEtalaseAndCampaignChip()
        delay()
        selectEtalase()
        delay()
        cassavaValidator.verify("click - etalase card")

        selectProduct()
        delay()
        cassavaValidator.verify("click - product card")

        clickSubmitProductTag()
        delay()
        cassavaValidator.verify("click - save product card")

        delay()
        cassavaValidator.verify("view - product selection summary")

        clickDeleteOnFirstProduct()
        delay()
        cassavaValidator.verify("click - delete a product tagged")

        clickCloseBottomSheet()
        delay()
        cassavaValidator.verify("click - back product selection page")

        clickMenuProduct()
        delay()
        clickNextOnProductPickerSummary()
        delay()
        cassavaValidator.verify("click - save product tag")

        clickMenuProduct()
        delay()
        clickAddMoreProduct()
        delay()
        cassavaValidator.verify("click - add product card")
    }

    private fun selectCampaign() {
        /** 0 for header, 1..x for the campaign */
        val firstCampaignIdx = 1

        selectEtalaseOrCampaign(firstCampaignIdx)
    }

    private fun selectEtalase() {
        /** 2 for campaign header + etalase header */
        val totalHeader = 2
        val firstEtalaseIdx = mockCampaignList.size + totalHeader

        selectEtalaseOrCampaign(firstEtalaseIdx)
    }
}
