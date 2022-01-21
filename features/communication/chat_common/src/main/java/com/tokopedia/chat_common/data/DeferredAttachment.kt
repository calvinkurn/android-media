package com.tokopedia.chat_common.data

interface DeferredAttachment {

    var isLoading: Boolean
    var isError: Boolean
    val id: String

    fun updateData(attribute: Any?)
    fun syncError()
    fun finishLoading()

    companion object {
        const val PAYLOAD_DEFERRED = "payload_deferred"
    }
}