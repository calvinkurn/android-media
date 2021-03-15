package com.tokopedia.devicefingerprint.appauth.usecase

import com.tokopedia.devicefingerprint.appauth.data.AppAuthResponse
import com.tokopedia.devicefingerprint.appauth.data.MutationSignDvcRequest
import com.tokopedia.devicefingerprint.submitdevice.usecase.SubmitDeviceInfoUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

class AppAuthUseCase @Inject constructor(
        repository: GraphqlRepository) : GraphqlUseCase<AppAuthResponse>(repository) {

    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(AppAuthResponse::class.java)
    }

    companion object {
        val query:String =
                """
            mutation mutationSignDvc(${'$'}input: MutationSignDvcRequest!){
              mutationSignDvc(input: ${'$'}input) {
                success
                error_message
              }
            }
        """.trimIndent()
    }

    fun setParams(content: String, version: String = "1") {
        val params: Map<String, Any?> = mutableMapOf(
                SubmitDeviceInfoUseCase.PARAM_INPUT to MutationSignDvcRequest(
                        version, content
                )
        )
        setRequestParams(params)
    }
}