package com.tokopedia.play.broadcaster.shorts.testcase.preparation.setup.product

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
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
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockAccountManager: PlayShortsAccountManager = mockk(relaxed = true)
    private val mockIdleManager: PlayShortsIdleManager = mockk(relaxed = true)

    private val uiModelBuilder = ShortsUiModelBuilder()

    private val mockShortsConfig = uiModelBuilder.buildShortsConfig()
    private val mockAccountList = uiModelBuilder.buildAccountListModel(usernameBuyer = false, tncBuyer = false)
    private val mockAccountShop = mockAccountList[0]
    private val mockEtalaseList = uiModelBuilder.buildEtalaseList()
    private val mockCampaignList = uiModelBuilder.buildCampaignList()
    private val mockProductTagSection = uiModelBuilder.buildProductTagSectionList()
    private val mockEtalaseProducts = uiModelBuilder.buildEtalaseProducts()

    init {
        coEvery { mockShortsRepo.getAccountList() } returns mockAccountList
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockAccountManager.isAllowChangeAccount(any()) } returns true
        coEvery { mockShortsRepo.getShortsConfiguration(any(), any()) } returns mockShortsConfig
        coEvery { mockBroRepo.getEtalaseList() } returns mockEtalaseList
        coEvery { mockBroRepo.getCampaignList() } returns mockCampaignList
        coEvery { mockBroRepo.getProductsInEtalase(any(), any(), any(), any()) } returns mockEtalaseProducts
        coEvery { mockBroRepo.setProductTags(any(), any()) } returns Unit
        coEvery { mockBroRepo.getProductTagSummarySection(any()) } returns mockProductTagSection
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
                        mockAccountManager = mockAccountManager,
                        mockUserSession = mockUserSession,
                        mockIdleManager = mockIdleManager,
                        mockRouter = mockk(relaxed = true),
                        mockDataStore = mockk(relaxed = true),
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
    fun testAnalytic_clickSearchBarOnProductSetup() {
        delay()
        clickSearchBarProductPickerSGC()
        delay()

        cassavaValidator.verify("click - search product")
    }

    @Test
    fun testAnalytic_clickProductSorting() {
        delay()
        clickSortChip()
        delay()

        cassavaValidator.verify("click - product sort")
    }

    @Test
    fun testAnalytic_clickCampaignAndEtalaseFilter() {

        delay()
        clickEtalaseAndCampaignChip()
        delay()

        cassavaValidator.verify("click - filter product etalase")
    }

    @Test
    fun testAnalytic_clickCloseOnProductChooser() {

        delay()
        clickCloseBottomSheet()
        delay()

        cassavaValidator.verify("click - close button on product bottom sheet")
    }

    @Test
    fun testAnalytic_clickSelectProductOnProductSetup() {

        delay()
        selectProduct()
        delay()

        cassavaValidator.verify("click - product card")
    }

    @Test
    fun testAnalytic_clickCloseOnProductSortingBottomSheet() {
        delay()
        clickSortChip()
        delay()
        clickCloseBottomSheet()
        delay()

        cassavaValidator.verify("click - close sort product")
    }

    @Test
    fun testAnalytic_clickProductSortingType() {

        delay()
        clickSortChip()
        delay()
        selectSortType()
        delay()
        clickSaveSort()
        delay()

        cassavaValidator.verify("click - sort type")
    }

    @Test
    fun testAnalytic_viewProductSortingBottomSheet() {

        delay()
        clickSortChip()
        delay()

        cassavaValidator.verify("view - sorting bottom sheet")
    }

    @Test
    fun testAnalytic_clickCloseOnProductFilterBottomSheet() {
        delay()
        clickEtalaseAndCampaignChip()
        delay()
        clickCloseBottomSheet()
        delay()

        cassavaValidator.verify("click - close filter bottom sheet")
    }

    @Test
    fun testAnalytic_clickCampaignCard() {
        
        /** 0 for header, 1..x for the campaign */
        val firstCampaignIdx = 1

        delay()
        clickEtalaseAndCampaignChip()
        delay()
        selectEtalaseOrCampaign(firstCampaignIdx)
        delay()

        cassavaValidator.verify("click - campaign card")
    }

    @Test
    fun testAnalytic_clickEtalaseCard() {
        
        /** 2 for campaign header + etalase header */
        val totalHeader = 2
        val firstEtalaseIdx = mockCampaignList.size + totalHeader

        delay()
        clickEtalaseAndCampaignChip()
        delay()
        selectEtalaseOrCampaign(firstEtalaseIdx)
        delay()

        cassavaValidator.verify("click - etalase card")
    }

    @Test
    fun testAnalytic_viewProductFilterBottomSheet() {
        delay()
        clickEtalaseAndCampaignChip()
        delay()

        cassavaValidator.verify("view - filter bottom sheet")
    }

    @Test
    fun testAnalytic_viewProductChooser() {
        delay()
        cassavaValidator.verify("view - product selection bottom sheet")
    }

    @Test
    fun testAnalytic_clickSaveButtonOnProductSetup() {

        delay()
        selectProduct()
        delay()
        clickSubmitProductTag()
        delay()

        cassavaValidator.verify("click - save product card")
    }

    @Test
    fun testAnalytic_clickAddMoreProductOnProductSetup() {

        delay()
        selectProduct()
        delay()
        clickSubmitProductTag()
        delay()
        clickAddMoreProduct()
        delay()

        cassavaValidator.verify("click - add product card")
    }

    @Test
    fun testAnalytic_clickCloseOnProductSummary() {

        delay()
        selectProduct()
        delay()
        clickSubmitProductTag()
        delay()
        clickCloseBottomSheet()
        delay()

        cassavaValidator.verify("click - back product selection page")
    }

    @Test
    fun testAnalytic_clickDeleteProductOnProductSetup() {

        delay()
        selectProduct()
        delay()
        clickSubmitProductTag()
        delay()
        clickDeleteOnFirstProduct()
        delay()

        cassavaValidator.verify("click - delete a product tagged")
    }

    @Test
    fun testAnalytic_clickDoneOnProductSetup() {

        delay()
        selectProduct()
        delay()
        clickSubmitProductTag()
        delay()
        clickNextOnProductPickerSummary()
        delay()

        cassavaValidator.verify("click - save product tag")
    }

    @Test
    fun testAnalytic_viewProductSummary() {

        delay()
        selectProduct()
        delay()
        clickSubmitProductTag()
        delay()

        cassavaValidator.verify("view - product selection summary")
    }
}
