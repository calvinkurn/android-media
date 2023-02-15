package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.chat_common.data.AttachInvoiceSentUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.view.uimodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class WebsocketSendTest : BaseTopChatViewModelTest() {

    @Test
    fun should_send_msg_to_ws() {
        // Given
        val preview = MessageUiModel.Builder().build()
        val payload = ""
        every {
            payloadGenerator.generatePreviewMsg(any(), any(), any(), any())
        } returns preview
        every {
            payloadGenerator.generateWsPayload(any(), any(), any(), any(), any(), any(), any())
        } returns payload

        // When
        webSocketViewModel.sendMsg("", null, null)
        webSocketViewModel.sendMsg("", null, null, listOf())

        // Then
        assertEquals(webSocketViewModel.previewMsg.value, preview)
        verify {
            chatWebSocket.sendPayload(payload)
        }
        verifySendStopTyping()
    }

    @Test
    fun should_send_sticker_to_ws() {
        // Given
        val preview = StickerUiModel.Builder().build()
        val payload = ""
        val sticker = Sticker()
        every {
            payloadGenerator.generateStickerPreview(any(), any(), any())
        } returns preview
        every {
            payloadGenerator.generateStickerWsPayload(any(), any(), any(), any(), any())
        } returns payload

        // When
        viewModel.getMessageId(testShopId, testUserId, source)
        webSocketViewModel.sendSticker(sticker, null)

        // Then
        assertEquals(webSocketViewModel.previewMsg.value, preview)
        verify {
            chatWebSocket.sendPayload(payload)
        }
        verifySendStopTyping()
    }

    @Test
    fun should_send_attachment_to_ws() {
        // Given
        val sendablePreview = TopchatProductAttachmentPreviewUiModel.Builder().build()
        val preview = StickerUiModel.Builder().build()
        val payload = ""
        every {
            payloadGenerator.generateAttachmentPreviewMsg(any(), any(), any())
        } returns preview
        every {
            payloadGenerator.generateAttachmentWsPayload(any(), any(), any(), any(), any())
        } returns payload

        // When
        viewModel.addAttachmentPreview(sendablePreview)
        webSocketViewModel.attachmentsPreview = arrayListOf(sendablePreview)
        webSocketViewModel.sendAttachments("a")

        // Then
        assertEquals(viewModel.attachmentsPreview.value, arrayListOf(sendablePreview))
        assertEquals(webSocketViewModel.previewMsg.value, preview)
        assertEquals(webSocketViewModel.attachmentSent.value, sendablePreview)
        verify {
            chatWebSocket.sendPayload(payload)
        }
    }

    @Test
    fun should_not_send_attachment_to_ws() {
        // When
        webSocketViewModel.sendAttachments("a")

        // Then
        assertEquals(webSocketViewModel.previewMsg.value, null)
        assertEquals(webSocketViewModel.attachmentSent.value, null)
    }

    @Test
    fun should_remove_srw_bubble_if_send_invoice() {
        // Given
        val sendablePreview = mockk<InvoicePreviewUiModel>()
        val preview = AttachInvoiceSentUiModel.Builder().build()
        val payload = ""
        every {
            payloadGenerator.generateAttachmentPreviewMsg(any(), any(), any())
        } returns preview
        every {
            payloadGenerator.generateAttachmentWsPayload(any(), any(), any(), any(), any())
        } returns payload

        // When
        viewModel.addAttachmentPreview(sendablePreview)
        webSocketViewModel.sendAttachments("a")

        // Then
        assertEquals(webSocketViewModel.removeSrwBubble.value, null)
    }

    @Test
    fun should_send_ws_start_typing() {
        // Given
        val payload = payloadGenerator.generateWsPayloadStartTyping(
            webSocketViewModel.roomMetaData.msgId
        )

        // When
        webSocketViewModel.sendWsStartTyping()

        // Then
        verify {
            chatWebSocket.sendPayload(payload)
        }
    }
}
