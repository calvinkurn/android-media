package com.tokopedia.topchat.chatsetting.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.topchat.chatsetting.data.GetChatSettingResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GetChatSettingUseCase(
        private val gqlQuery: String,
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<GetChatSettingResponse>() {

    private val params = RequestParams.EMPTY

    override suspend fun executeOnBackground(): GetChatSettingResponse {
        val gqlRequest = GraphqlRequest(gqlQuery, GetChatSettingResponse::class.java, params.parameters)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD)
                        .build()
        )

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GetChatSettingResponse::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(GetChatSettingResponse::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

}