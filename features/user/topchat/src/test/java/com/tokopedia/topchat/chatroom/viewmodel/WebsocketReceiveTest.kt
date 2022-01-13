package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class WebsocketReceiveTest: BaseTopChatViewModelTest() {

    @Test
    fun on_destroy_activity() {
        // When
        viewModel.onDestroy()

        // Then
        verify {
            chatWebSocket.close()
            chatWebSocket.destroy()
        }
    }
}