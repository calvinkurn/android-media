package com.tokopedia.play.broadcaster.setup.productchooser

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.helper.contains
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 04/03/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductChooserAnalyticTest {

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

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

    init {
        coEvery { mockRepo.getProductsInEtalase(any(), any(), any(), any()) } returns PagedDataUiModel(
            dataList = mockSelectedProducts,
            hasNextPage = false,
        )
    }

    @Test
    fun testAnalytic_closeBottomSheet() {
        val robot = ProductChooserRobot {
            productSetupViewModel(
                productSectionList = emptyList(),
                repo = mockRepo,
            )
        }

        robot.close()


        assertThat(
            cassavaTestRule.validate(analyticFile),
            contains(
                "eventAction" to "click - close button on product bottom sheet"
            )
        )
    }

    @Test
    fun testAnalytic_closeBottomSheet_confirm() {
        val robot = ProductChooserRobot {
            productSetupViewModel(
                productSectionList = mockProductSections,
                repo = mockRepo,
            )
        }

        with(robot) {
            selectProduct()
            close()
            confirmClose()
        }

        assertThat(
            cassavaTestRule.validate(analyticFile),
            contains(
                "eventAction" to "click - confirm close on add product page"
            )
        )
    }

    @Test
    fun testAnalytic_closeBottomSheet_cancel() {
        val robot = ProductChooserRobot {
            productSetupViewModel(
                productSectionList = mockProductSections,
                repo = mockRepo,
            )
        }

        with(robot) {
            selectProduct()
            close()
            cancelClose()
        }

        assertThat(
            cassavaTestRule.validate(analyticFile),
            contains(
                "eventAction" to "click - cancel close on add product page"
            )
        )
    }
}