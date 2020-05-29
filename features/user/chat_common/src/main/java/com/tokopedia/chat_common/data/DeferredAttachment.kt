package com.tokopedia.chat_common.data

interface DeferredAttachment {

    open var isLoading: Boolean
    val id: String

    fun updateData(attributes: String)

    companion object {
        const val PAYLOAD_DEFERRED = "payload_deferred"
    }
}