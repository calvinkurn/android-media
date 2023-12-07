package com.tokopedia.content.product.picker.testcase.summary

import com.tokopedia.content.product.picker.builder.CommonUiModelBuilder
import com.tokopedia.content.product.picker.builder.ProductSetupUiModelBuilder
import com.tokopedia.content.product.picker.robot.ContentProductPickerSellerViewModelRobot
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductChooserEvent
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductSetupAction
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductTagSummaryUiModel
import com.tokopedia.content.product.picker.util.assertEqualTo
import com.tokopedia.content.product.picker.util.assertFalse
import com.tokopedia.content.product.picker.util.assertTrue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions

/**
 * Created By : Jonathan Darwin on February 18, 2022
 */
class SetupProductSummaryViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: ContentProductPickerSellerRepository = mockk(relaxed = true)

    /** Mock Response */
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val commonUiModelBuilder = CommonUiModelBuilder()

    private val mockProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList()

    private val mockException = commonUiModelBuilder.buildException()

    /** Summary Page */
    @Test
    fun `when user successfully delete product, it should emit success state`() {
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSectionList
        coEvery { mockRepo.setProductTags(any(), any()) } returns Unit

        val robot = ContentProductPickerSellerViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent {
                robot.submitAction(ProductSetupAction.DeleteSelectedProduct(mockProductTagSectionList[0].products[0]))
            }

            state.productCount.assertEqualTo(mockProductTagSectionList.sumOf { it.products.size })
            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Success)
            state.productTagSectionList.assertEqualTo(mockProductTagSectionList)
            event[0].assertEqualTo(ProductChooserEvent.DeleteProductSuccess(1))
        }
    }

    @Test
    fun `when user failed delete product, it should emit fail state`() {
        coEvery { mockRepo.setProductTags(any(), any()) } throws mockException

        val robot = ContentProductPickerSellerViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent {
                robot.submitAction(ProductSetupAction.DeleteSelectedProduct(mockProductTagSectionList[0].products[0]))
            }

            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Unknown)
            Assertions.assertTrue(event[0] is ProductChooserEvent.DeleteProductError)
        }
    }

    // Product Numeration
    @Test
    fun `when user in live broadcaster, product numeration is shown`() {
        val vm = ContentProductPickerSellerViewModelRobot(
            isNumerationShown = true,
            dispatchers = testDispatcher
        )
        vm.use {
            it.getViewModel().isNumerationShown.assertTrue()
        }
    }

    @Test
    fun `when user in short video, product numeration is hidden`() {
        val vm = ContentProductPickerSellerViewModelRobot(
            isNumerationShown = false,
            dispatchers = testDispatcher
        )
        vm.use {
            it.getViewModel().isNumerationShown.assertFalse()
        }
    }

    @Test
    fun `when user in unknown video, product numeration is hidden`() {
        val vm = ContentProductPickerSellerViewModelRobot(
            isNumerationShown = false,
            dispatchers = testDispatcher
        )
        vm.use {
            it.getViewModel().isNumerationShown.assertFalse()
        }
    }
}
