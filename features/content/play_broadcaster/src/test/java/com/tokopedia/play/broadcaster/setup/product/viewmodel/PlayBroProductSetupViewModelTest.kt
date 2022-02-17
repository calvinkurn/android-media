package com.tokopedia.play.broadcaster.setup.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.robot.PlayBroProductSetupViewModelRobot
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.setup.product.model.ProductTagSummaryUiModel
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertType
import com.tokopedia.play.broadcaster.util.isEqualTo
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * Created By : Jonathan Darwin on February 17, 2022
 */
internal class PlayBroProductSetupViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    /** Mock Response */
    private val mockProductTagSection = List(5) {
        ProductTagSectionUiModel("Test 1", CampaignStatus.Ongoing, List(3) {
            ProductUiModel("$it", "Product 1", "", 10, OriginalPrice("Rp 12.000", 12000.0))
        })
    }
    private val mockProductCount = mockProductTagSection.sumOf { it.products.size }


    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /** Summary Page */
    @Test
    fun `when user successfully load product section, it should emit success state`() {

        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSection

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordSummaryState{
                robot.submitAction(ProductSetupAction.LoadProductSummary)
            }

            state.productCount.assertEqualTo(mockProductCount)
            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Success)
            state.productTagSectionList.assertEqualTo(mockProductTagSection)
        }
    }

    @Test
    fun `when user failed load product section, it should emit error state`() {

        val exception = Exception("Network Error")
        coEvery { mockRepo.getProductTagSummarySection(any()) } throws exception

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent{
                robot.submitAction(ProductSetupAction.LoadProductSummary)
            }

            state.productCount.assertEqualTo(0)
            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Unknown)
            state.productTagSectionList.assertEqualTo(emptyList())
            assertTrue(event[0] is PlayBroProductChooserEvent.GetDataError)
        }
    }

    @Test
    fun `when user successfully delete product, it should emit success state`() {

        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSection
        coEvery { mockRepo.setProductTags(any(), any()) } returns Unit

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent{
                robot.submitAction(ProductSetupAction.DeleteSelectedProduct(mockProductTagSection[0].products[0]))
            }

            state.productCount.assertEqualTo(mockProductCount)
            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Success)
            state.productTagSectionList.assertEqualTo(mockProductTagSection)
            event[0].assertEqualTo(PlayBroProductChooserEvent.DeleteProductSuccess(1))
        }
    }

    @Test
    fun `when user failed delete product, it should emit fail state`() {

        val exception = Exception("Network Error")
        coEvery { mockRepo.setProductTags(any(), any()) } throws exception

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent{
                robot.submitAction(ProductSetupAction.DeleteSelectedProduct(mockProductTagSection[0].products[0]))
            }

            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Unknown)
            assertTrue(event[0] is PlayBroProductChooserEvent.DeleteProductError)
        }
    }
}