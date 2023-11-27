package com.tokopedia.stories.creation.testcase.productpicker.seller

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.stories.creation.builder.ProductPickerModelBuilder
import com.tokopedia.stories.creation.provider.ProductPickerTestActivityProvider
import com.tokopedia.test.application.annotations.CassavaTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on October 19, 2023
 */
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductPickerSellerAnalyticTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    /** Helper */
    private val robot = ProductPickerSellerRobot(targetContext, cassavaTestRule)

    /** Builder */
    private val productPickerModelBuilder = ProductPickerModelBuilder()

    /** Mock Class */
    private val mockRepository: ContentProductPickerSellerRepository = mockk(relaxed = true)
    private val mockCommonRepository: ProductPickerSellerCommonRepository = mockk(relaxed = true)

    /** Mock Data */
    private val mockEtalaseList = productPickerModelBuilder.buildEtalaseList()
    private val mockCampaignList = productPickerModelBuilder.buildCampaignList()
    private val mockEtalaseProductList = productPickerModelBuilder.buildEtalaseProducts()
    private val mockCampaignProductList = productPickerModelBuilder.buildEtalaseProducts()
    private val mockTaggedProductList = productPickerModelBuilder.buildProductTagSectionList()

    @Before
    fun setUp() {
        ProductPickerTestActivityProvider.mockRepository = mockRepository
        ProductPickerTestActivityProvider.mockCommonRepository = mockCommonRepository

        coEvery { mockCommonRepository.getEtalaseList() } returns mockEtalaseList
        coEvery { mockCommonRepository.getCampaignList() } returns mockCampaignList
        coEvery { mockRepository.getProductsInEtalase(any(), any(), any(), any()) } returns mockEtalaseProductList
        coEvery { mockCommonRepository.getProductsInCampaign(any(), any()) } returns mockCampaignProductList
        coEvery { mockRepository.getProductTagSummarySection(any(), any()) } returns mockTaggedProductList
    }

    @Test
    fun testAnalytic_storiesCreation_productPickerSeller() {
        robot.launchActivity()
            .verifyAction("view - product selection bottom sheet")
            .selectProduct()
            .verifyAction("click - product")
            .clickSearchBarProductPickerSGC()
            .verifyAction("click - search product")
            .closeSoftKeyboard()
            .clickSortChip()
            .verifyAction("click - sort product")
            .selectSortType()
            .verifyAction("click - sorting option")
            .clickSaveSort()
            .clickEtalaseAndCampaignChip()
            .performDelay()
            .verifyAction("click - filter product")
            .selectEtalaseOrCampaign(1)
            .performDelay()
            .verifyAction("click - campaign option")
            .clickEtalaseAndCampaignChip()
            .performDelay()
            .selectEtalaseOrCampaign(3)
            .verifyAction("click - etalase option")
            .performDelay()
            .clickSubmitProductTag()
            .verifyAction("click - save product selection")
            .performDelay(1000)
            .verifyAction("view - product selection summary bottom sheet")
            .clickDeleteOnFirstProduct()
            .verifyAction("click - delete selected product")
            .clickNextOnProductPickerSummary()
            .verifyAction("click - save selesai product selection")
    }
}
