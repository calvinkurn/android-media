package com.tokopedia.devicefingerprint.datavisor.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.devicefingerprint.datavisor.response.SubmitDeviceInitResponse
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class SubmitDVTokenUseCase @Inject constructor(val repository: dagger.Lazy<GraphqlRepository>) {

    var useCase: GraphqlUseCase<SubmitDeviceInitResponse>? = null

    companion object {
        private const val PARAM_INPUT = "input"
        private const val ANDROID = "android"
    }

    private fun getOrCreateUseCase(): GraphqlUseCase<SubmitDeviceInitResponse> {
        val useCaseTemp = useCase
        if (useCaseTemp == null) {
            val newUseCase = GraphqlUseCase<SubmitDeviceInitResponse>(repository.get())
            newUseCase.setGraphqlQuery(SubDvcIntlEventQuery())
            newUseCase.setTypeClass(SubmitDeviceInitResponse::class.java)
            useCase = newUseCase
            return newUseCase
        } else {
            return useCaseTemp
        }
    }

    suspend fun execute(
        key: String, retryCount: Int, errorMessage: String,
        deviceType: String = ANDROID,
        checkForce: Boolean = false
    ): SubmitDeviceInitResponse {
        val useCase = getOrCreateUseCase()
        val params = mapOf(
            PARAM_INPUT to SubDvcIntlEventRequest(
                key, retryCount,
                errorMessage, deviceType, checkForce
            )
        )
        useCase.setRequestParams(params)
        return useCase.executeOnBackground()
    }
}

data class SubDvcIntlEventRequest(
    @SerializedName("key")
    val key: String,
    @SerializedName("retry_count")
    val retryCount: Int,
    @SerializedName("error_message")
    val errorMessage: String,
    @SerializedName("device_type")
    val deviceType: String,
    @SerializedName("check_force_initialize")
    val checkForceInitialize: Boolean
)

class SubDvcIntlEventQuery : GqlQueryInterface {

    override fun getOperationNameList(): List<String> {
        return listOf("subDvcIntlEvent")
    }

    override fun getQuery() =
        "mutation subDvcIntlEvent(\$input: SubDvcIntlEventRequest!){ subDvcIntlEvent(input: \$input) { is_error data { is_expire } } }"

    override fun getTopOperationName(): String {
        return "subDvcIntlEvent"
    }
}