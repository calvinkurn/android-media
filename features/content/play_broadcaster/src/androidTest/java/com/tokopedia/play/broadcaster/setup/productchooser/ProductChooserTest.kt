package com.tokopedia.play.broadcaster.setup.productchooser

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.test.application.annotations.UiTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 02/03/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@UiTest
class ProductChooserTest {

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockSection = List(1) {
        ProductTagSectionUiModel("", CampaignStatus.Ongoing, List(2) {
            ProductUiModel(it.toString(), "Product $it", "", 1, OriginalPrice("Rp1000.00", 1000.0))
        })
    }
    private val mockProduct = mockSection.flatMap { it.products }

    private val listener = object : ProductChooserBottomSheet.Listener {
        override fun onSetupCancelled(bottomSheet: ProductChooserBottomSheet) {
            bottomSheet.dismiss()
        }

        override fun onSetupSuccess(bottomSheet: ProductChooserBottomSheet) {

        }

        override fun openCampaignAndEtalaseList(bottomSheet: ProductChooserBottomSheet) {

        }
    }

    init {
        coEvery { mockRepo.getProductsInEtalase(any(), any(), any(), any()) } returns PagedDataUiModel(
            dataList = mockProduct,
            hasNextPage = false,
        )
    }

    @Test
    fun testCloseBottomSheet_whenNoSelectedProduct() {
        val robot = ProductChooserRobot(listener) {
            productSetupViewModel(
                productSectionList = emptyList(),
                repo = mockRepo,
            )
        }

        robot.close()
        robot.assertExitDialog(isShown = false)
        robot.assertBottomSheet(isOpened = false)
    }

    @Test
    fun testCloseBottomSheet_whenHasSelectedProduct_andThenNoChangeProduct() {
        val robot = ProductChooserRobot(listener) {
            productSetupViewModel(
                productSectionList = mockSection,
                repo = mockRepo,
            )
        }

        robot.close()
        robot.assertExitDialog(isShown = false)
        robot.assertBottomSheet(isOpened = false)
        robot.assertExitDialog(isShown = false)
        robot.assertBottomSheet(isOpened = false)
    }

    @Test
    fun testCloseBottomSheet_whenHasSelectedProduct_andThenChangeProduct() {
        val robot = ProductChooserRobot(listener) {
            productSetupViewModel(
                productSectionList = mockSection,
                repo = mockRepo,
            )
        }

        robot.selectProduct(0)
            .close()

        robot.assertExitDialog(isShown = true)
    }

    @Test
    fun testCloseBottomSheet_whenHasSelectedProduct_andThenNoMoreProductSelected() {
        val robot = ProductChooserRobot(listener) {
            productSetupViewModel(
                productSectionList = mockSection,
                repo = mockRepo,
            )
        }

        robot.selectProduct(0)
            .selectProduct(1)
            .close()

        robot.assertExitDialog(isShown = false)
        robot.assertBottomSheet(isOpened = false)
    }
}
