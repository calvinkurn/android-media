package com.tokopedia.devicefingerprint.appauth.usecase

import com.tokopedia.devicefingerprint.appauth.data.AppAuthResponse
import com.tokopedia.devicefingerprint.appauth.data.MutationSignDvcRequest
import com.tokopedia.devicefingerprint.submitdevice.usecase.SubmitDeviceInfoUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

class AppAuthUseCase @Inject constructor(val repository: dagger.Lazy<GraphqlRepository>) {

    var useCase: GraphqlUseCase<AppAuthResponse>? = null

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


    private fun getOrCreateUseCase() : GraphqlUseCase<AppAuthResponse> {
        val useCaseTemp = useCase
        if (useCaseTemp == null) {
            val newUseCase = GraphqlUseCase<AppAuthResponse>(repository.get())
            newUseCase.setGraphqlQuery(query)
            newUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            newUseCase.setTypeClass(AppAuthResponse::class.java)
            useCase = newUseCase
            return newUseCase
        } else {
            return useCaseTemp
        }
    }

    suspend fun execute(content: String, version: String = "1"): AppAuthResponse{
        val useCase = getOrCreateUseCase()
        val params: Map<String, Any?> = mutableMapOf(
                SubmitDeviceInfoUseCase.PARAM_INPUT to MutationSignDvcRequest(
                        version, content
                )
        )
        useCase.setRequestParams(params)
        return useCase.executeOnBackground()
    }
}