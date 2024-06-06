package com.tokopedia.play.broadcaster.setup.productchooser

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.model.OriginalPrice
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatus
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.pinnedproduct.PinProductUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.broadcaster.helper.containsEventAction
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.test.application.annotations.CassavaTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 04/03/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@CassavaTest
class ProductChooserAnalyticTest {

    private val mockRepo: ContentProductPickerSellerRepository = mockk(relaxed = true)

    private val mockSelectedProducts = List(3) {
        ProductUiModel(
            id = it.toString(),
            name = "Product $it",
            imageUrl = "",
            stock = 1,
            price = OriginalPrice("Rp ${it}000", it * 1000.0),
            hasCommission = false,
            commissionFmt = "",
            commission = 0L,
            extraCommission = false,
            pinStatus = PinProductUiModel.Empty,
            number = "",
            shopName = "",
            shopBadge = "",
            rating = "",
            countSold = ""
        )
    }

    private val mockProductSections = listOf(
        ProductTagSectionUiModel(
            name = "Section Test",
            campaignStatus = CampaignStatus.Ongoing,
            products = mockSelectedProducts
        )
    )

    init {
        coEvery { mockRepo.getProductsInEtalase(any(), any(), any(), any()) } returns PagedDataUiModel(
            dataList = mockSelectedProducts,
            hasNextPage = false
        )
    }

    private fun createRobot() = ProductChooserRobot {
        productSetupViewModel(
            productSectionList = mockProductSections,
            repo = mockRepo
        )
    }

    @Test
    fun testAnalytic_productChooserSGC() {
        createRobot()
            .selectProduct(0)
            .assertEventAction("click - product card")

            .clickSortChips()
            .assertEventAction("click - product sort")

            .selectSort(0)
            .saveSort()
            .assertEventAction("click - sort type")

            .clickEtalaseCampaignChips()
            .assertEventAction("click - campaign & etalase filter")

            .searchKeyword("abc")
            .performDelay()
            .assertEventAction("click - search bar")

            .saveProducts()
            .assertEventAction("click - save product card")

            .selectProduct(0)
            .close()
            .assertEventAction("click - close button on product bottom sheet")

            .cancelClose()
            .assertEventAction("click - cancel close on add product page")

            .close()
            .confirmClose()
            .assertEventAction("click - confirm close on add product page")
    }
}
