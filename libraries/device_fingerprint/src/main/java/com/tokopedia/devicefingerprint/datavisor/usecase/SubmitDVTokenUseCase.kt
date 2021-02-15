package com.tokopedia.devicefingerprint.datavisor.usecase

import com.tokopedia.devicefingerprint.datavisor.pojo.VisorResponse
import com.tokopedia.devicefingerprint.submitdevice.payload.InsertDeviceInfoPayload
import com.tokopedia.devicefingerprint.submitdevice.usecase.SubmitDeviceInfoUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SubmitDVTokenUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<VisorResponse>)
    : UseCase<VisorResponse>() {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(VisorResponse::class.java)
    }
    companion object {
        const val PARAM_INPUT = "input"
        private val query = """
            mutation subDvcIntlEvent(${'$'}input: SubUDInfRequest!){
              subUDinf(input: ${'$'}input) {
                is_error
              }
            }
        """.trimIndent()
    }

    private var params: RequestParams = RequestParams.create()

    override suspend fun executeOnBackground(): VisorResponse {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParams(payload: InsertDeviceInfoPayload) {
        val params: Map<String, Any?> = mutableMapOf(
                SubmitDeviceInfoUseCase.PARAM_INPUT to payload
        )
        setRequestParams(params)
    }

    fun setParams(type: String = "android", token: String) {
        params.parameters.clear()
        params.putString(PARAM_TYPE, type)
        params.putString(PARAM_CONTENT, token)
    }
}