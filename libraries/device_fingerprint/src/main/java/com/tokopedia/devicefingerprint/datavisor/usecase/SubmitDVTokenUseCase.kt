package com.tokopedia.devicefingerprint.datavisor.usecase

import com.tokopedia.devicefingerprint.datavisor.response.SubmitDeviceInitResponse
import com.tokopedia.gql_query_annotation.GqlQuery
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
        private const val PARAM_EVENT = "event"
        private const val ANDROID = "android"
        private const val QUERY = """
            mutation subDvcIntlEvent(
                ${'$'}key: String!, 
                ${'$'}retry_count: Int!, 
                ${'$'}error_message: String!, 
                ${'$'}device_type: String!,
                ${'$'}event: String!
            ){
              subDvcIntlEvent(
                input: {
                    key: ${'$'}key, 
                    retry_count: ${'$'}retry_count, 
                    error_message: ${'$'}error_message, 
                    device_type: ${'$'}device_type,
                    event: ${'$'}event,
                }
              ) {
                is_error
              }
            }
        """
    }

    @GqlQuery("SubDvcIntlEvent", QUERY)
    private fun getOrCreateUseCase(): GraphqlUseCase<SubmitDeviceInitResponse> {
        val useCaseTemp = useCase
        if (useCaseTemp == null) {
            val newUseCase = GraphqlUseCase<SubmitDeviceInitResponse>(repository.get())
            newUseCase.setGraphqlQuery(SubDvcIntlEvent())
            newUseCase.setTypeClass(SubmitDeviceInitResponse::class.java)
            useCase = newUseCase
            return newUseCase
        } else {
            return useCaseTemp
        }
    }

    suspend fun execute(
        key: String,
        retryCount: Int,
        errorMessage: String,
        event: String,
        deviceType: String = ANDROID,
    ): SubmitDeviceInitResponse {
        val useCase = getOrCreateUseCase()
        val params: Map<String, Any?> = mutableMapOf(
            PARAM_KEY to key,
            PARAM_RETRY_COUNT to retryCount,
            PARAM_ERROR_MESSAGE to errorMessage,
            PARAM_DEVICE_TYPE to deviceType,
            PARAM_EVENT to event,
        )
        useCase.setRequestParams(params)
        return useCase.executeOnBackground()
    }
}