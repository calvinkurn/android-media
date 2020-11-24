package com.tokopedia.devicefingerprint.crysp.usecase

import com.tokopedia.devicefingerprint.crysp.model.CryspResponse
import com.tokopedia.devicefingerprint.datavisor.pojo.VisorResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SubmitCrTokenUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<CryspResponse>)
    : UseCase<CryspResponse>() {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(CryspResponse::class.java)
    }
    companion object {

        const val PARAM_TYPE = "type"
        const val PARAM_CONTENT = "content"
    }

    private var params: RequestParams = RequestParams.create()

    //region query
    private val query by lazy {
        val type = "\$type"
        val content = "\$content"

        """
           mutation CryspMutation($type: String!, $content: String!){
              getDeviceCrDetail(input: {
                type: $type,
                content: $content}) {
                   status
                   message
              }
        }""".trimIndent()
    }
    //endregion

    override suspend fun executeOnBackground(): CryspResponse {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParams(type: String = "device", content: String) {
        params.parameters.clear()
        params.putString(PARAM_TYPE, type)
        params.putString(PARAM_CONTENT, content)
    }
}