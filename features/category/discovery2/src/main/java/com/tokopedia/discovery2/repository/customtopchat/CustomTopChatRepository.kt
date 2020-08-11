package com.tokopedia.discovery2.repository.customtopchat

import com.tokopedia.discovery2.data.customtopchatdatamodel.CustomChatResponse

interface CustomTopChatRepository {
    suspend fun getMessageId(queryParameterMap: MutableMap<String, Any>): CustomChatResponse?
}