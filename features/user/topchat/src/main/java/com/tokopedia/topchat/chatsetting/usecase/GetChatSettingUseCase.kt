package com.tokopedia.topchat.chatsetting.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatsetting.data.GetChatSettingResponse
import com.tokopedia.usecase.RequestParams

class GetChatSettingUseCase(
        private val gqlQuery: String,
        private val gqlUseCase: GraphqlUseCase<GetChatSettingResponse>
) {

    private val params = RequestParams.EMPTY

    fun get(onSuccess: (GetChatSettingResponse) -> Unit, onError: (Throwable) -> Unit) {
        gqlUseCase.apply {
            setTypeClass(GetChatSettingResponse::class.java)
            setRequestParams(params.parameters)
            setGraphqlQuery(gqlQuery)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

//    override suspend fun executeOnBackground(): GetChatSettingResponse {
//        val gqlRequest = GraphqlRequest(gqlQuery, GetChatSettingResponse::class.java, params.parameters)
//        gqlUseCase.clearRequest()
//        gqlUseCase.addRequest(gqlRequest)
//        gqlUseCase.setCacheStrategy(
//                GraphqlCacheStrategy
//                        .Builder(CacheType.ALWAYS_CLOUD)
//                        .build()
//        )
//
//        val gqlResponse = gqlUseCase.executeOnBackground()
//        val error = gqlResponse.getError(GetChatSettingResponse::class.java)
//        if (error == null || error.isEmpty()) {
//            return gqlResponse.getData(GetChatSettingResponse::class.java)
//        } else {
//            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
//        }
//    }

}