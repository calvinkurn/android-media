package com.tokopedia.devicefingerprint.appauth.usecase

import com.tokopedia.devicefingerprint.appauth.data.AppAuthResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class AppAuthUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<AppAuthResponse>)
    : UseCase<AppAuthResponse>() {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(AppAuthResponse::class.java)
    }
    companion object {

        const val PARAM_VERSION = "version"
        const val PARAM_CONTENT = "content"
    }

    private var params: RequestParams = RequestParams.create()

    private val query =
        """
           mutation mutationSignDvc(${'$'}version: String!, ${'$'}content: String!){
              mutationSignDvc(input: {
                version: ${'$'}version,
                content: ${'$'}content}) {
                   status
                   message
              }
        }""".trimIndent()

    override suspend fun executeOnBackground(): AppAuthResponse {
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParams(content: String, version: String = "1") {
        params.putString(PARAM_VERSION, version)
        params.putString(PARAM_CONTENT, content)
    }
}