package com.tokopedia.play.broadcaster.shorts.testcase.preparation

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.content.test.util.pressBack
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
class PlayShortsPreparationAnalyticTest {

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
        coEvery { mockContentProductPickerSGCCommonRepo.getEtalaseList() } returns emptyList()
        coEvery { mockContentProductPickerSGCCommonRepo.getCampaignList() } returns emptyList()
        coEvery { mockContentProductPickerSGCRepo.getProductsInEtalase(any(), any(), any(), any()) } returns mockEtalaseProducts
        coEvery { mockContentProductPickerSGCRepo.setProductTags(any(), any()) } returns Unit
        coEvery { mockContentProductPickerSGCRepo.getProductTagSummarySection(any()) } returns mockProductTagSection

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

    @Before
    fun setUp() {
        launcher.launchActivity()
    }

    @Test
    fun testAnalytic_shorts_preparationPage() {
        cassavaValidator.verify("view - post creation page")

        clickMenuTitle()
        cassavaValidator.verify("click - edit title")
        completeTitleMenu()

        clickMenuProduct()
        cassavaValidator.verify("click - add product tag")
        completeProductMenu()

        clickMenuCover()
        cassavaValidator.verify("click - add cover on preparation page")
        pressBack()

        clickClosePreparationPage()
        cassavaValidator.verify("view - yakin mau keluar botom sheet")

        clickContinueOnLeaveConfirmationPopup()
        cassavaValidator.verify("click - lanjut persiapan bottom sheet")

        clickContinueOnPreparationPage()
        cassavaValidator.verify("click - lanjut post creation")
    }
}
