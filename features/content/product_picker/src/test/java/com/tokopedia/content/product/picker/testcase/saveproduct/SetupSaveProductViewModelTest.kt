package com.tokopedia.content.product.picker.testcase.saveproduct

import com.tokopedia.content.product.picker.builder.CommonUiModelBuilder
import com.tokopedia.content.product.picker.builder.ProductSetupUiModelBuilder
import com.tokopedia.content.product.picker.robot.ContentProductPickerSGCViewModelRobot
import com.tokopedia.content.product.picker.seller.domain.ContentProductPickerSGCRepository
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductChooserEvent
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductSetupAction
import com.tokopedia.content.product.picker.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 18, 2022
 */
class SetupSaveProductViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: ContentProductPickerSGCRepository = mockk(relaxed = true)

    /** Mock Response */
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val commonUiModelBuilder = CommonUiModelBuilder()

    private val mockProduct = productSetupUiModelBuilder.buildProductUiModel()
    private val mockProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList()

    private val mockSelectedProducts = mockProductTagSectionList.flatMap { it.products }

    private val mockException = commonUiModelBuilder.buildException()

    @Test
    fun `when user wants to save products and success, it should trigger event success`() {
        val robot = ContentProductPickerSGCViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSectionList

        robot.use {
            val (state, event) = robot.recordStateAsListAndEvent {
                submitAction(ProductSetupAction.ToggleSelectProduct(mockProduct))
                submitAction(ProductSetupAction.SaveProducts)
            }

            state[2].saveState.isLoading.assertEqualTo(true)
            state[3].selectedProductList.assertEqualTo(mockSelectedProducts)
            state.last().saveState.isLoading.assertEqualTo(false)
            event.last().assertEqualTo(ProductChooserEvent.SaveProductSuccess)
        }
    }

    @Test
    fun `when user wants to save products and success, it should load product section tag`() {
        val robot = ContentProductPickerSGCViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSectionList

        robot.use {
            runBlockingTest {
                robot.submitAction(ProductSetupAction.ToggleSelectProduct(mockProduct))
            }

            val state = robot.recordSummaryState {
                robot.submitAction(ProductSetupAction.SaveProducts)
            }

            state.productTagSectionList.assertEqualTo(mockProductTagSectionList)
        }
    }

    @Test
    fun `when user wants to save products and failed, it should trigger trigger event error`() {

        coEvery { mockRepo.setProductTags(any(), any()) } throws mockException

        val robot = ContentProductPickerSGCViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordStateAsListAndEvent {
                robot.submitAction(ProductSetupAction.ToggleSelectProduct(mockProduct))
                robot.submitAction(ProductSetupAction.SaveProducts)
            }

            state[state.lastIndex-1].saveState.isLoading.assertEqualTo(true)
            state.last().saveState.isLoading.assertEqualTo(false)
            event.last().assertEqualTo(ProductChooserEvent.ShowError(mockException))
        }
    }
}
