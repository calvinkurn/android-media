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
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.websocket.response.PlayMerchantVoucherSocketResponse
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

class PlayVoucherTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val modelBuilder = UiModelBuilder.get()

    private val gson = Gson()

    @Test
    fun `given empty voucher, when on init, then it should return empty voucher`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        val emptyVoucherList = emptyList<MerchantVoucherUiModel>()
        val emptyVoucher = channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                voucher = modelBuilder.buildVoucherModel(
                    voucherList = emptyVoucherList
                )
            )
        )
        every { repo.getChannelData(any()) } returns emptyVoucher

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = repo
        )

        robot.use {
            val state = it.recordState {}
            state.tagItems.voucher.voucherList
                .assertEqualTo(emptyVoucherList)
        }
    }

    @Test
    fun `given some vouchers, when on init, then it should return those same vouchers`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        val mockVoucherList = List(3) {
            modelBuilder.buildMerchantVoucher(
                id = it.toString(),
                title = "Voucher $it",
            )
        }
        val mockVoucher = channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                voucher = modelBuilder.buildVoucherModel(voucherList = mockVoucherList)
            )
        )
        every { repo.getChannelData(any()) } returns mockVoucher

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = repo
        )

        robot.use {
            val state = it.recordState {}
            state.tagItems.voucher.voucherList
                .assertEqualTo(mockVoucherList)
        }
    }

    @Test
    fun `given voucher can be shown, when page is focused, then it should return vouchers from network`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        every { repo.getChannelData(any()) } returns channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(canShow = true)
            )
        )

        val mockVoucherList = List(3) {
            modelBuilder.buildMerchantVoucher(
                id = it.toString(),
                title = "Voucher $it",
            )
        }
        val mockTagItem = modelBuilder.buildTagItem(
            voucher = modelBuilder.buildVoucherModel(
                voucherList = mockVoucherList
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
            state.tagItems.voucher.voucherList
                .assertEqualTo(mockVoucherList)
        }
    }

    @Test
    fun `given voucher cannot be shown, when page is focused, then it should return initial voucher`() {
        val repo: PlayViewerRepository = mockk(relaxed = true)
        val initialVoucherList = emptyList<MerchantVoucherUiModel>()
        every { repo.getChannelData(any()) } returns channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    canShow = false
                ),
                voucher = modelBuilder.buildVoucherModel(
                    voucherList = initialVoucherList
                )
            )
        )

        val mockVoucherList = List(3) {
            modelBuilder.buildMerchantVoucher(
                id = it.toString(),
                title = "Voucher $it",
            )
        }
        val mockTagItem = modelBuilder.buildTagItem(
            voucher = modelBuilder.buildVoucherModel(
                voucherList = mockVoucherList
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
            state.tagItems.voucher.voucherList
                .assertEqualTo(initialVoucherList)
            state.tagItems.voucher.voucherList
                .assertNotEqualTo(mockVoucherList)
        }
    }

    @Test
    fun `when received new vouchers from socket, then it should update the vouchers`() {
        val voucherSize = 2
        val voucherBaseTitle = "Voucher Test"
        val voucherTagSocketJson = PlayMerchantVoucherSocketResponse.generateResponse(
            size = voucherSize,
            title = voucherBaseTitle,
        )

        val mockVoucherSocketResponse = gson.fromJson(
            voucherTagSocketJson,
            WebSocketResponse::class.java
        )

        val mockSocket: PlayWebSocket = mockk(relaxed = true)
        val socketFlow = MutableSharedFlow<WebSocketAction>()

        every { mockSocket.listenAsFlow() } returns socketFlow

        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        every { mockRepo.getChannelData(any()) } returns channelDataBuilder.buildChannelData(
            tagItems = modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(emptyList(), false),
                voucher = modelBuilder.buildVoucherModel(emptyList())
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
                    WebSocketAction.NewMessage(mockVoucherSocketResponse)
                )
            }
            state.tagItems.voucher.voucherList
                .size
                .assertEqualTo(voucherSize)

            state.tagItems.voucher.voucherList
                .forEachIndexed { index, voucher ->
                    voucher.title.assertEqualTo("$voucherBaseTitle ${index+1}%")
                }
        }
    }
}