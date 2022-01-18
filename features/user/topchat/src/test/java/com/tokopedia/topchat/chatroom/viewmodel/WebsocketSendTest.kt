package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.attachcommon.preview.ProductPreview
import com.tokopedia.chat_common.data.AttachInvoiceSentUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class WebsocketSendTest: BaseTopChatViewModelTest() {

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
        viewModel.sendMsg("", null, null)
        viewModel.sendMsg("", null, null, listOf())

        // Then
        assertEquals(viewModel.previewMsg.value, preview)
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
        viewModel.sendSticker(sticker, null)

        // Then
        assertEquals(viewModel.previewMsg.value, preview)
        verify {
            chatWebSocket.sendPayload(payload)
        }
        verifySendStopTyping()
    }

    @Test
    fun should_send_attachment_to_ws() {
        // Given
        val sendablePreview = SendableProductPreview(ProductPreview())
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
        viewModel.sendAttachments("a")

        // Then
        assertEquals(viewModel.previewMsg.value, preview)
        assertEquals(viewModel.attachmentSent.value, sendablePreview)
        verify {
            chatWebSocket.sendPayload(payload)
        }
    }

    @Test
    fun should_not_send_attachment_to_ws() {
        // When
        viewModel.sendAttachments("a")

        // Then
        assertEquals(viewModel.previewMsg.value, null)
        assertEquals(viewModel.attachmentSent.value, null)
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
        viewModel.sendAttachments("a")

        // Then
        assertEquals(viewModel.removeSrwBubble.value, null)
    }

    @Test
    fun should_send_ws_start_typing() {
        // Given
        val payload = payloadGenerator.generateWsPayloadStartTyping(
            viewModel.roomMetaData.msgId
        )

        // When
        viewModel.sendWsStartTyping()

        // Then
        verify {
            chatWebSocket.sendPayload(payload)
        }
    }
}