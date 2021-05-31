package com.tokopedia.devicefingerprint.submitdevice.usecase

import com.tokopedia.devicefingerprint.submitdevice.response.SubmitDeviceInfoResponse
import com.tokopedia.devicefingerprint.submitdevice.utils.InsertDeviceInfoPayloadCreator
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class SubmitDeviceInfoUseCase @Inject constructor(
        val repository: dagger.Lazy<GraphqlRepository>,
        val creator: dagger.Lazy<InsertDeviceInfoPayloadCreator>) {

    var useCase: GraphqlUseCase<SubmitDeviceInfoResponse>? = null

    companion object {
        const val PARAM_INPUT = "input"
        private val query = """
            mutation submitDeviceInfo(${'$'}input: SubUDInfRequest!){
              subUDinf(input: ${'$'}input) {
                is_error
              }
            }
        """.trimIndent()
    }

    private fun getOrCreateUseCase(): GraphqlUseCase<SubmitDeviceInfoResponse> {
        val useCaseTemp = useCase
        if (useCaseTemp == null) {
            val newUseCase = GraphqlUseCase<SubmitDeviceInfoResponse>(repository.get())
            newUseCase.setGraphqlQuery(query)
            newUseCase.setTypeClass(SubmitDeviceInfoResponse::class.java)
            useCase = newUseCase
            return newUseCase
        } else {
            return useCaseTemp
        }
    }

    suspend fun execute(): SubmitDeviceInfoResponse {
        val useCase = getOrCreateUseCase()
        val params: Map<String, Any?> = mutableMapOf(
                PARAM_INPUT to creator.get().create()
        )
        useCase.setRequestParams(params)
        return useCase.executeOnBackground()
    }

}
