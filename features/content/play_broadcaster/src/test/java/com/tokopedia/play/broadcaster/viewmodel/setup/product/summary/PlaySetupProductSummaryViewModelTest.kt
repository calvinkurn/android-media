package com.tokopedia.play.broadcaster.viewmodel.setup.product.summary

import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroProductSetupViewModelRobot
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.setup.product.model.ProductTagSummaryUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions

/**
 * Created By : Jonathan Darwin on February 18, 2022
 */
class PlaySetupProductSummaryViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    /** Mock Response */
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val uiModelBuilder = UiModelBuilder()

    private val mockProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList()

    private val mockException = uiModelBuilder.buildException()

    /** Summary Page */
    @Test
    fun `when user successfully delete product, it should emit success state`() {
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSectionList
        coEvery { mockRepo.setProductTags(any(), any()) } returns Unit

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent {
                robot.submitAction(ProductSetupAction.DeleteSelectedProduct(mockProductTagSectionList[0].products[0]))
            }

            state.productCount.assertEqualTo(mockProductTagSectionList.sumOf { it.products.size })
            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Success)
            state.productTagSectionList.assertEqualTo(mockProductTagSectionList)
            event[0].assertEqualTo(PlayBroProductChooserEvent.DeleteProductSuccess(1))
        }
    }

    @Test
    fun `when user failed delete product, it should emit fail state`() {
        coEvery { mockRepo.setProductTags(any(), any()) } throws mockException

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent {
                robot.submitAction(ProductSetupAction.DeleteSelectedProduct(mockProductTagSectionList[0].products[0]))
            }

            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Unknown)
            Assertions.assertTrue(event[0] is PlayBroProductChooserEvent.DeleteProductError)
        }
    }

    // Product Numeration
    @Test
    fun `when user in live broadcaster, product numeration is shown`() {
        val vm = PlayBroProductSetupViewModelRobot(
            isNumerationShown = true,
            dispatchers = testDispatcher
        )
        vm.use {
            it.getViewModel().isNumerationShown.assertTrue()
        }
    }

    @Test
    fun `when user in short video, product numeration is hidden`() {
        val vm = PlayBroProductSetupViewModelRobot(
            isNumerationShown = false,
            dispatchers = testDispatcher
        )
        vm.use {
            it.getViewModel().isNumerationShown.assertFalse()
        }
    }

    @Test
    fun `when user in unknown video, product numeration is hidden`() {
        val vm = PlayBroProductSetupViewModelRobot(
            isNumerationShown = false,
            dispatchers = testDispatcher
        )
        vm.use {
            it.getViewModel().isNumerationShown.assertFalse()
        }
    }
}
