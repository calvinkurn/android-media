package com.tokopedia.play.viewmodel.tagitem

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertNotEqualTo
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class PlayProductTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val modelBuilder = UiModelBuilder.get()

    @Test
    fun `given empty product, when on init, then it should return empty product`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        val emptyProductList = emptyList<PlayProductUiModel.Product>()
        val emptyProduct = channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(productList = emptyProductList)
            )
        )
        every { repo.getChannelData(any()) } returns emptyProduct

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = repo
        )

        robot.use {
            val state = it.recordState {}
            state.tagItems.product.productList
                .assertEqualTo(emptyProductList)
        }
    }

    @Test
    fun `given some products, when on init, then it should return those same products`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        val mockProductList = List(3) {
            modelBuilder.buildProduct(
                id = it.toString(),
                title = "Product $it",
            )
        }
        val mockProduct = channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(productList = mockProductList)
            )
        )
        every { repo.getChannelData(any()) } returns mockProduct

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = repo
        )

        robot.use {
            val state = it.recordState {}
            state.tagItems.product.productList
                .assertEqualTo(mockProductList)
        }
    }

    @Test
    fun `given product can be shown, when page is focused, then it should return products from network`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        every { repo.getChannelData(any()) } returns channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(canShow = true)
            )
        )

        val mockProductList = List(3) {
            modelBuilder.buildProduct(
                id = it.toString(),
                title = "Product $it",
            )
        }
        val mockTagItem = modelBuilder.buildTagItem(
            product = modelBuilder.buildProductModel(
                productList = mockProductList
            )
        )
        coEvery { repo.getTagItem(any()) } returns mockTagItem

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = repo
        )

        robot.use {
            val state = it.recordState {
                focusPage(mockk(relaxed = true))
            }
            state.tagItems.product.productList
                .assertEqualTo(mockProductList)
        }
    }

    @Test
    fun `given product cannot be shown, when page is focused, then it should return initial product`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        val initialProductList = emptyList<PlayProductUiModel.Product>()
        every { repo.getChannelData(any()) } returns channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = initialProductList,
                    canShow = false
                )
            )
        )

        val mockProductList = List(3) {
            modelBuilder.buildProduct(
                id = it.toString(),
                title = "Product $it",
            )
        }
        val mockTagItem = modelBuilder.buildTagItem(
            product = modelBuilder.buildProductModel(
                productList = mockProductList
            )
        )
        coEvery { repo.getTagItem(any()) } returns mockTagItem

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = repo
        )

        robot.use {
            val state = it.recordState {
                focusPage(mockk(relaxed = true))
            }
            state.tagItems.product.productList
                .assertEqualTo(initialProductList)
            state.tagItems.product.productList
                .assertNotEqualTo(mockProductList)
        }
    }
}