package com.tokopedia.devicefingerprint.datavisor.usecase

import com.google.gson.Gson
import com.tokopedia.devicefingerprint.datavisor.payload.DeviceInitPayload
import com.tokopedia.devicefingerprint.datavisor.response.SubmitDeviceInitResponse
import com.tokopedia.devicefingerprint.submitdevice.utils.ContentCreator
import com.tokopedia.encryption.security.md5
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class SubmitDVTokenUseCase @Inject constructor(
        repository: GraphqlRepository,
        private val gson: Gson,
        private val contentCreator: ContentCreator)
    : GraphqlUseCase<SubmitDeviceInitResponse>(repository) {

    companion object {
        private const val PARAM_CONTENT = "content"
        private const val PARAM_IDENTIFIER = "identifier"
        private const val PARAM_VERSION = "version"
        private val query = """
            mutation subDvcIntlEvent(${'$'}content: String!, ${'$'}identifier: String!, ${'$'}version: String!){
              subDvcIntlEvent(input: {content: ${'$'}content, identifier: ${'$'}identifier, version: ${'$'}version}) {
                is_error
              }
            }
        """.trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(SubmitDeviceInitResponse::class.java)
    }

    fun setParams(payload: DeviceInitPayload) {
        val json = gson.toJson(payload)
        val params: Map<String, Any?> = mutableMapOf(
                PARAM_CONTENT to contentCreator.createContent(json),
                PARAM_IDENTIFIER to json.md5(),
                PARAM_VERSION to "1"
        )
        setRequestParams(params)
    }
}