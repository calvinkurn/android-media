package com.tokopedia.chat_common.data

interface DeferredAttachment {

    open var isLoading: Boolean
    val id: String

    fun updateData(attribute: Any?)

    companion object {
        const val PAYLOAD_DEFERRED = "payload_deferred"
    }
}