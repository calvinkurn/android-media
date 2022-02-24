package com.tokopedia.play.broadcaster.viewmodel.setup.product.saveproduct

import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroProductSetupViewModelRobot
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 18, 2022
 */
class PlaySetupSaveProductViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    /** Mock Response */
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val uiModelBuilder = UiModelBuilder()

    private val mockProduct = productSetupUiModelBuilder.buildProductUiModel()
    private val mockProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList()

    private val mockException = uiModelBuilder.buildException()

    @Test
    fun `when user wants to save products and success, it should trigger event success`() {
        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSectionList

        robot.use {
            runBlockingTest {
                robot.submitAction(ProductSetupAction.SelectProduct(mockProduct))
            }

            val (state, event) = robot.recordStateAsListAndEvent {
                robot.submitAction(ProductSetupAction.SaveProducts)
            }

            state[1].saveState.isLoading.assertEqualTo(true)
            state[2].selectedProductSectionList.assertEqualTo(mockProductTagSectionList)
            state[3].saveState.isLoading.assertEqualTo(false)
            event.last().assertEqualTo(PlayBroProductChooserEvent.SaveProductSuccess)
        }
    }

    @Test
    fun `when user wants to save products and success, it should load product section tag`() {
        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSectionList

        robot.use {
            runBlockingTest {
                robot.submitAction(ProductSetupAction.SelectProduct(mockProduct))
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

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            runBlockingTest {
                robot.submitAction(ProductSetupAction.SelectProduct(mockProduct))
            }

            val (state, event) = robot.recordStateAsListAndEvent {
                robot.submitAction(ProductSetupAction.SaveProducts)
            }

            state[1].saveState.isLoading.assertEqualTo(true)
            state[2].saveState.isLoading.assertEqualTo(false)
            event.last().assertEqualTo(PlayBroProductChooserEvent.ShowError(mockException))
        }
    }
}