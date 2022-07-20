package com.tokopedia.topchat.stub.chattemplate.usecase.api

import com.google.gson.JsonObject
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateDataWrapper
import com.tokopedia.topchat.common.chat.api.ChatTemplateApi
import kotlinx.coroutines.delay
import javax.inject.Inject

class ChatTemplateApiStub @Inject constructor(): ChatTemplateApi {

    var delay = 0L
    var templateResponse: TemplateData = TemplateData().apply {
        isIsEnable = false
        isSuccess = false
        templates = listOf()
    }
    var error: MessageErrorException? = null

    override suspend fun getTemplateSuspend(
        parameters: Map<String, Boolean>
    ): TemplateDataWrapper<TemplateData> {
        return getResultOrError()
    }

    override suspend fun setTemplate(
        parameters: JsonObject,
        isSeller: Boolean
    ): TemplateDataWrapper<TemplateData> {
        return getResultOrError()
    }

    override suspend fun editTemplate(
        index: Int,
        jsonObject: Map<String, Any>,
        isSeller: Boolean
    ): TemplateDataWrapper<TemplateData> {
        return getResultOrError()
    }

    override suspend fun createTemplate(
        parameters: Map<String, Any>
    ): TemplateDataWrapper<TemplateData> {
        return getResultOrError()
    }

    override suspend fun deleteTemplate(
        index: Int,
        isSeller: Boolean,
        parameters: JsonObject
    ): TemplateDataWrapper<TemplateData> {
        return getResultOrError()
    }

    private suspend fun getResultOrError(): TemplateDataWrapper<TemplateData> {
        error?.let { throw it }
        val result = TemplateDataWrapper(data = templateResponse)
        return if (delay > 0) {
            delay(delay)
            result
        } else {
            result
        }
    }
}