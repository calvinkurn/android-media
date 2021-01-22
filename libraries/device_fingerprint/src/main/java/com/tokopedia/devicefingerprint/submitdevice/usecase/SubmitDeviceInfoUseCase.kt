package com.tokopedia.devicefingerprint.submitdevice.usecase

import com.tokopedia.devicefingerprint.submitdevice.payload.InsertDeviceInfoPayload
import com.tokopedia.devicefingerprint.submitdevice.response.SubmitDeviceInfoResponse
import com.tokopedia.devicefingerprint.submitdevice.utils.InsertDeviceInfoPayloadCreator
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class SubmitDeviceInfoUseCase @Inject constructor(
    repository: GraphqlRepository
): GraphqlUseCase<SubmitDeviceInfoResponse>(repository) {

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

    init {
        setGraphqlQuery(query)
        setTypeClass(SubmitDeviceInfoResponse::class.java)
    }

    fun setParams(payload: InsertDeviceInfoPayload) {
        val params: Map<String, Any?> = mutableMapOf(
                PARAM_INPUT to payload
        )
        setRequestParams(params)
    }

}
