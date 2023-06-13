package com.tokopedia.tkpd.flashsale.data.response

sealed class SSEStatus {
    data class Success(val sseResponse: SSEResponse): SSEStatus()
    data class Close(val reason: SSECloseReason): SSEStatus()
}
