package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.customtopchatdatamodel.CustomChatResponse
import com.tokopedia.discovery2.repository.customtopchat.CustomTopChatRepository
import javax.inject.Inject

class CustomTopChatUseCase @Inject constructor(private val customTopChatRepository: CustomTopChatRepository) {

    suspend fun getCustomTopChatMessageId(queryParameterMap: MutableMap<String, Any>): CustomChatResponse? {
        return customTopChatRepository.getMessageId(queryParameterMap)
    }
}