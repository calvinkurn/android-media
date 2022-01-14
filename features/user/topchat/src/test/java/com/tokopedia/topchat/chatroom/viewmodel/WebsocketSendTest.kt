package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import io.mockk.every
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
}