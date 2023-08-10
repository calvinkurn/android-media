package com.tokopedia.play.broadcaster.setup.productchooser

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.helper.containsEventAction
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.broadcaster.ui.model.pinnedproduct.PinProductUiModel
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

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

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
        )
    }

    private val mockProductSections = listOf(
        ProductTagSectionUiModel(
            name = "Section Test",
            campaignStatus = CampaignStatus.Ongoing,
            products = mockSelectedProducts,
        )
    )

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val analyticFile = "tracker/content/playbroadcaster/play_broadcaster_analytic.json"

    init {
        coEvery { mockRepo.getProductsInEtalase(any(), any(), any(), any()) } returns PagedDataUiModel(
            dataList = mockSelectedProducts,
            hasNextPage = false,
        )
    }

    private fun createRobot() = ProductChooserRobot {
        productSetupViewModel(
            productSectionList = mockProductSections,
            repo = mockRepo,
        )
    }

    @Test
    fun testAnalytic_closeBottomSheet() {
        val robot = createRobot()

        robot.close()

        assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - close button on product bottom sheet")
        )
    }

    @Test
    fun testAnalytic_closeBottomSheet_confirm() {
        val robot = createRobot()

        with(robot) {
            selectProduct()
            close()
            confirmClose()
        }

        assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - confirm close on add product page")
        )
    }

    @Test
    fun testAnalytic_closeBottomSheet_cancel() {
        val robot = createRobot()

        with(robot) {
            selectProduct()
            close()
            cancelClose()
        }

        assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - cancel close on add product page")
        )
    }

    @Test
    fun testAnalytic_saveProduct() {
        val robot = createRobot()

        robot.selectProduct(0)
            .saveProducts()

        assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - save product card")
        )
    }

    @Test
    fun testAnalytic_selectProduct() {
        val robot = createRobot()

        robot.selectProduct()

        assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - product card")
        )
    }

    @Test
    fun testAnalytic_clickSortChips() {
        val robot = createRobot()

        robot.clickSortChips()

        assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - product sort")
        )
    }

    @Test
    fun testAnalytic_clickEtalaseCampaignChips() {
        val robot = createRobot()

        robot.clickEtalaseCampaignChips()

        assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - campaign & etalase filter")
        )
    }

    @Test
    fun testAnalytic_searchKeyword() {
        val robot = createRobot()

        with(robot) {
            searchKeyword("abc")
            delay()
        }

        assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - search bar")
        )
    }

    @Test
    fun testAnalytic_selectSort() {
        val robot = createRobot()

        with(robot) {
            clickSortChips()
            selectSort(0)
            saveSort()
        }

        assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - sort type")
        )
    }
}
