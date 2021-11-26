package com.tokopedia.play_common.sse.model

/**
 * Created By : Jonathan Darwin on September 08, 2021
 */
sealed class SSEAction {
    data class Message(val message: SSEResponse): SSEAction()
    data class Close(val reason: SSECloseReason): SSEAction()
}