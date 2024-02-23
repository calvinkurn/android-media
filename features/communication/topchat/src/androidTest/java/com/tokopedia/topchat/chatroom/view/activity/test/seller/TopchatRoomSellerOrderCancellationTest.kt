package com.tokopedia.topchat.chatroom.view.activity.test.seller

import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.BaseSellerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.invoiceResult
import com.tokopedia.topchat.chatroom.view.activity.robot.msgBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.orderCancellationResult
import com.tokopedia.topchat.chatroom.view.activity.robot.orderCancellationRobot
import com.tokopedia.topchat.common.alterResponseOf
import com.tokopedia.topchat.common.websocket.FakeTopchatWebSocket
import com.tokopedia.websocket.WebSocketResponse
import org.junit.Test

@UiTest
class TopchatRoomSellerOrderCancellationTest: BaseSellerTopchatRoomTest() {

    @Test
    fun assert_widget_order_cancellation() {
        // Given
        getChatUseCase.response = getChatUseCase.sellerOrderCancellationBefore
        chatAttachmentUseCase.response = chatAttachmentUseCase.sellerOrderCancellationBeforeAttachment

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        invoiceResult {
            assertInvoiceAt(2, "Pesanan Diproses")
        }
        msgBubbleResult {
            assertBubbleMsg(
                1,
                withText("Yaah, pembeli telah membatalkan pesanan ini. Yuk, segera tanggapi pembatalannya.")
            )
        }
        orderCancellationResult {
            assertOrderCancellationWidgetAt(0, "Tanggapi Pembatalan")
        }

        // Given
        getChatUseCase.response = getChatUseCase.sellerOrderCancellationAfter
        chatAttachmentUseCase.response = chatAttachmentUseCase.sellerOrderCancellationAfterAttachment

        // When
        orderCancellationRobot {
            clickOrderCancellationWidgetAt(0)
        }
        simulateWebSocketRefresh()
        Thread.sleep(1000)

        // Then
        generalResult {
            openPageWithApplink(
                "tokopedia://seller/buyer-request-cancel-respond?orderId=167963098&statusCode=400&statusText=Pesanan Diproses&reason=Ingin mengubah produk yang dibeli&description=Alasan pembeli mengajukan pembatalan pesanan:&primaryButtonText=Batalkan Pesanan&secondaryButtonText=Proses Pesanan&primaryButtonKey=reject_order&secondaryButtonKey=accept_order&pageSource=chat"
            )
        }
        invoiceResult {
            assertInvoiceAt(2, "Dibatalkan Penjual")
        }
        msgBubbleResult {
            assertBubbleMsg(
                0,
                withText("Kamu telah setuju dengan pembatalan pesanan ini. Tenang aja, Skor Performa Tokomu tidak akan berkurang.")
            )
        }
        orderCancellationResult {
            assertOrderCancellationWidgetGoneAt(0)
        }
    }

    @Test
    fun assert_widget_order_cancellation_ws() {
        // Given
        getChatUseCase.response = getChatUseCase.emptySellerOrderCancellation
        chatAttachmentUseCase.response = chatAttachmentUseCase.sellerOrderCancellationBeforeAttachment

        // When
        launchChatRoomActivity()
        stubIntents()
        simulateOrderCancellation()

        // Then
        invoiceResult {
            assertInvoiceAt(2, "Pesanan Diproses")
        }
        msgBubbleResult {
            assertBubbleMsg(
                1,
                withText("Yaah, pembeli telah membatalkan pesanan ini. Yuk, segera tanggapi pembatalannya.")
            )
        }
        orderCancellationResult {
            assertOrderCancellationWidgetAt(0, "Tanggapi Pembatalan")
        }
    }

    private fun simulateWebSocketRefresh() {
        val refreshEvent = websocket.generateRefreshResponse(MSG_ID)
        websocket.simulateResponse(refreshEvent)
    }

    private fun simulateOrderCancellation() {
        val invoiceWs = alterResponseOf<WebSocketResponse>(
            "ws/seller/order_cancellation/ws_seller_order_cancellation_invoice.json") {
            val data = it.getAsJsonObject(FakeTopchatWebSocket.data)
            data.addProperty(FakeTopchatWebSocket.msg_id, MSG_ID)
        }
        val autoReplyWs = alterResponseOf<WebSocketResponse>(
            "ws/seller/order_cancellation/ws_seller_order_cancellation_auto_reply.json") {
            val data = it.getAsJsonObject(FakeTopchatWebSocket.data)
            data.addProperty(FakeTopchatWebSocket.msg_id, MSG_ID)
        }
        val widgetWs = alterResponseOf<WebSocketResponse>(
            "ws/seller/order_cancellation/ws_seller_order_cancellation_widget.json") {
            val data = it.getAsJsonObject(FakeTopchatWebSocket.data)
            data.addProperty(FakeTopchatWebSocket.msg_id, MSG_ID)
        }
        websocket.simulateResponse(invoiceWs)
        websocket.simulateResponse(autoReplyWs)
        websocket.simulateResponse(widgetWs)
    }
}
