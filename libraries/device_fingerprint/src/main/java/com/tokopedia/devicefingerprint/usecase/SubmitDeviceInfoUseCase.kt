package com.tokopedia.devicefingerprint.usecase

import com.tokopedia.devicefingerprint.payload.InsertDeviceInfoPayload
import com.tokopedia.devicefingerprint.response.SubmitDeviceInfoResponse
import com.tokopedia.devicefingerprint.utils.InsertDeviceInfoPayloadCreator
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class SubmitDeviceInfoUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val  insertDeviceInfoPayloadCreator: InsertDeviceInfoPayloadCreator
): GraphqlUseCase<SubmitDeviceInfoResponse>(repository) {

    companion object {
        const val PARAM_INPUT = "input"
        private val query = """
            mutation {
                subUDinf(${'$'}input: {
                    content: "",
                    identifier: "",
                    version: ""
                }) {
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

    override suspend fun executeOnBackground(): SubmitDeviceInfoResponse {
        setParams(insertDeviceInfoPayloadCreator.create())
        return super.executeOnBackground()
    }

}
