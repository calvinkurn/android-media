package com.tokopedia.devicefingerprint.datavisor.usecase

import com.tokopedia.devicefingerprint.datavisor.response.SubmitDeviceInitResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class SubmitDVTokenUseCase @Inject constructor(
        repository: GraphqlRepository)
    : GraphqlUseCase<SubmitDeviceInitResponse>(repository) {

    companion object {
        private const val PARAM_KEY = "key"
        private const val PARAM_RETRY_COUNT = "retry_count"
        private const val PARAM_ERROR_MESSAGE = "error_message"
        private const val PARAM_DEVICE_TYPE = "device_type"
        private const val ANDROID = "android"
        private val query = """
            mutation subDvcIntlEvent(${'$'}key: String!, ${'$'}retry_count: Int!, ${'$'}error_message: String!, ${'$'}device_type: String! ){
              subDvcIntlEvent(input: {key: ${'$'}key, retry_count: ${'$'}retry_count, error_message: ${'$'}error_message, device_type: ${'$'}device_type}) {
                is_error
              }
            }
        """.trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(SubmitDeviceInitResponse::class.java)
    }

    fun setParams(key: String, retryCount: Int, errorMessage: String, deviceType: String = ANDROID) {
        val params: Map<String, Any?> = mutableMapOf(
                PARAM_KEY to key,
                PARAM_RETRY_COUNT to retryCount,
                PARAM_ERROR_MESSAGE to errorMessage,
                PARAM_DEVICE_TYPE to deviceType
        )
        setRequestParams(params)
    }
}