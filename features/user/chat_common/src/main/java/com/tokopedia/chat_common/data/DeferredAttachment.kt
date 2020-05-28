package com.tokopedia.chat_common.data

interface DeferredAttachment {

    open var isLoading: Boolean
    val id: String

    fun updateData(attributes: String)
}