package com.tokopedia.tokomart.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.tokomart.home.domain.query.GetHomeLayoutData
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetHomeLayoutDataUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetHomeLayoutResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_CHANNEL_ID = "channelId"
    }

    init {
        setGraphqlQuery(GetHomeLayoutData.QUERY)
        setTypeClass(GetHomeLayoutResponse::class.java)
    }

    suspend fun execute(channelId: String): HomeLayoutResponse {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_CHANNEL_ID, channelId)
        }.parameters)

        val response = executeOnBackground().response
        val data = response.data.firstOrNull()
        return data ?: throw MessageErrorException()
    }
}