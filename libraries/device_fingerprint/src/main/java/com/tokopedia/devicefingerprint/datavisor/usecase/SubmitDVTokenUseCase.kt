package com.tokopedia.devicefingerprint.datavisor.usecase

import com.tokopedia.devicefingerprint.datavisor.response.SubmitDeviceInitResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class SubmitDVTokenUseCase @Inject constructor(val repository: dagger.Lazy<GraphqlRepository>) {

    var useCase: GraphqlUseCase<SubmitDeviceInitResponse>? = null

    companion object {
        private const val PARAM_KEY = "key"
        private const val PARAM_RETRY_COUNT = "retry_count"
        private const val PARAM_ERROR_MESSAGE = "error_message"
        private const val PARAM_DEVICE_TYPE = "device_type"
        private const val PARAM_CHECK_FORCE = "check_force_initialize"
        private const val ANDROID = "android"
        private val query = """
            mutation subDvcIntlEvent(${'$'}key: String!, 
                ${'$'}retry_count: Int!, 
                ${'$'}error_message: String!, 
                ${'$'}device_type: String!,
                ${'$'}check_force_initialize: Boolean){
              subDvcIntlEvent(input: {key: ${'$'}key, 
                retry_count: ${'$'}retry_count, 
                error_message: ${'$'}error_message, 
                device_type: ${'$'}device_type},
                check_force_initialize: ${'$'}check_force_initialize) {
                is_error
                data {
                  is_expire
                }
              }
            }
        """.trimIndent()
    }

    private fun getOrCreateUseCase(): GraphqlUseCase<SubmitDeviceInitResponse> {
        val useCaseTemp = useCase
        if (useCaseTemp == null) {
            val newUseCase = GraphqlUseCase<SubmitDeviceInitResponse>(repository.get())
            newUseCase.setGraphqlQuery(query)
            newUseCase.setTypeClass(SubmitDeviceInitResponse::class.java)
            useCase = newUseCase
            return newUseCase
        } else {
            return useCaseTemp
        }
    }

    suspend fun execute(key: String, retryCount: Int, errorMessage: String,
                        deviceType: String = ANDROID,
                        checkForce: Boolean = false): SubmitDeviceInitResponse {
        val useCase = getOrCreateUseCase()
        val params: Map<String, Any?> = mutableMapOf(
                PARAM_KEY to key,
                PARAM_RETRY_COUNT to retryCount,
                PARAM_ERROR_MESSAGE to errorMessage,
                PARAM_DEVICE_TYPE to deviceType,
                PARAM_CHECK_FORCE to checkForce
        )
        useCase.setRequestParams(params)
        return useCase.executeOnBackground()
    }
}