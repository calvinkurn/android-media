package com.tokopedia.play.broadcaster.setup.productsummary

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.play.broadcaster.helper.contains
import com.tokopedia.play.broadcaster.helper.containsEventAction
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.test.application.annotations.CassavaTest
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 08/03/22
 */
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductSummaryAnalyticTest {

    private val mockSelectedProducts = List(3) {
        ProductUiModel(
            id = it.toString(),
            name = "Product $it",
            imageUrl = "",
            stock = 1,
            price = OriginalPrice("Rp ${it}000", it * 1000.0),
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

    private fun createRobot() = ProductSummaryRobot {
        productSetupViewModel(
            productSectionList = mockProductSections,
            repo = mockk(relaxed = true),
        )
    }

    @Test
    fun testAnalytic_clickAddMoreProducts() {
        val robot = createRobot()

        robot.addMoreProduct()

        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - add product card")
        )
    }

    @Test
    fun testAnalytic_clickDeleteProduct() {
        val robot = createRobot()

        robot.deleteProduct()

        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - delete a product tagged")
        )
    }

    @Test
    fun testAnalytic_clickDone() {
        val robot = createRobot()

        robot.clickDone()

        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - save product tag")
        )
    }
}
