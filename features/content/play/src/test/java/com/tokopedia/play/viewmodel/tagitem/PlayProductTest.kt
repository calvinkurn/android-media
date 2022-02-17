package com.tokopedia.play.viewmodel.tagitem

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertNotEqualTo
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.websocket.response.PlayProductTagSocketResponse
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Rule
import org.junit.Test

class PlayProductTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val modelBuilder = UiModelBuilder.get()

    private val gson = Gson()

    @Test
    fun `given empty section, when on init, then it should return empty section`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        val emptyProductList = emptyList<ProductSectionUiModel>()
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
            state.tagItems.product.productSectionList
                .assertEqualTo(emptyProductList)
        }
    }

    @Test
    fun `given some sections, when on init, then it should return those same sections`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        val mockProductList = List(3) {
            modelBuilder.buildProductSection(
                productList = listOf(),
                config = ProductSectionUiModel.ConfigUiModel(
                    type = ProductSectionType.Upcoming,
                    startTime = "",
                    endTime = "",
                    serverTime = "",
                    timerInfo = "Dimulai dalam",
                    background = ProductSectionUiModel.BackgroundUiModel(gradients = emptyList(),
                        imageUrl = "\"https://ecs7.tokopedia.net/img/cache/700/product-1/2017/4/3/5510248/5510248_1fada4fe-8444-4911-b3e0-b70b54b119b6_1500_946.jpg\""),
                    title = "L'oreal New Launch"
                )
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
            state.tagItems.product.productSectionList
                .assertEqualTo(mockProductList)
        }
    }

    @Test
    fun `given product section can be shown, when page is focused, then it should return sections from network`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        every { repo.getChannelData(any()) } returns channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(canShow = true)
            )
        )

        val mockProductList = List(3) {
            modelBuilder.buildProductSection(
                productList = listOf(),
                config = ProductSectionUiModel.ConfigUiModel(
                    type = ProductSectionType.Upcoming,
                    startTime = "",
                    endTime = "",
                    serverTime = "",
                    timerInfo = "Dimulai dalam",
                    background = ProductSectionUiModel.BackgroundUiModel(gradients = emptyList(),
                        imageUrl = "\"https://ecs7.tokopedia.net/img/cache/700/product-1/2017/4/3/5510248/5510248_1fada4fe-8444-4911-b3e0-b70b54b119b6_1500_946.jpg\""),
                    title = "L'oreal New Launch"
                )
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
            state.tagItems.product.productSectionList
                .assertEqualTo(mockProductList)
        }
    }

    @Test
    fun `given product section cannot be shown, when page is focused, then it should return initial product`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        val initialProductList = emptyList<ProductSectionUiModel>()
        every { repo.getChannelData(any()) } returns channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = initialProductList,
                    canShow = false
                )
            )
        )

        val mockProductList = List(3) {
            modelBuilder.buildProductSection(
                productList = listOf(),
                config = ProductSectionUiModel.ConfigUiModel(
                    type = ProductSectionType.Upcoming,
                    startTime = "",
                    endTime = "",
                    serverTime = "",
                    timerInfo = "Dimulai dalam",
                    background = ProductSectionUiModel.BackgroundUiModel(gradients = emptyList(),
                        imageUrl = "\"https://ecs7.tokopedia.net/img/cache/700/product-1/2017/4/3/5510248/5510248_1fada4fe-8444-4911-b3e0-b70b54b119b6_1500_946.jpg\""),
                    title = "L'oreal New Launch"
                )
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
            state.tagItems.product.productSectionList
                .assertEqualTo(initialProductList)
            state.tagItems.product.productSectionList
                .assertNotEqualTo(mockProductList)
        }
    }

    @Test
    fun `when received new products from socket, then it should update the products`() {
        val sectionSize = 2
        val sectionTitle = "Section Test"
        val productTagSocketJson = PlayProductTagSocketResponse.generateResponseSection(
            size = sectionSize,
            title = sectionTitle,
        )

        val mockProductsSocketResponse = gson.fromJson(
            productTagSocketJson,
            WebSocketResponse::class.java
        )

        val mockSocket: PlayWebSocket = mockk(relaxed = true)
        val socketFlow = MutableSharedFlow<WebSocketAction>()

        every { mockSocket.listenAsFlow() } returns socketFlow

        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        every { mockRepo.getChannelData(any()) } returns channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(emptyList(), true)
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            playChannelWebSocket = mockSocket,
        )

        robot.use {
            val state = it.recordState {
                focusPage(mockk(relaxed = true))
                socketFlow.emit(
                    WebSocketAction.NewMessage(mockProductsSocketResponse)
                )
            }
            state.tagItems.product.productSectionList
                .size
                .assertEqualTo(sectionSize)

            state.tagItems.product.productSectionList
                .forEachIndexed { index, section ->
                    section.config.title.assertEqualTo("$sectionTitle ${index + 1}")
                }

            state.tagItems.product.canShow
                .assertTrue()
        }
    }
}