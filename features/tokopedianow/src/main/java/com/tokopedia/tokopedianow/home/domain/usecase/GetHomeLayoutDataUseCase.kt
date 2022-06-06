package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.home.domain.mapper.LocationParamMapper.mapLocation
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.query.GetHomeLayoutData
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Dynamic Home Channel Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/381550603/HPB+Home+-+Mojito+Channel
 */

class GetHomeLayoutDataUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetHomeLayoutResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_TOKEN = "token"
        private const val PARAM_NUM_OF_CHANNEL = "numOfChannel"
        private const val PARAM_LOCATION = "location"

        private const val DEFAULT_TOKEN = ""
        private const val DEFAULT_NUM_OF_CHANNEL = 10
    }

    init {
        setGraphqlQuery(GetHomeLayoutData.QUERY)
        setTypeClass(GetHomeLayoutResponse::class.java)
    }

    suspend fun execute(
        token: String = DEFAULT_TOKEN,
        numOfChannel: Int = DEFAULT_NUM_OF_CHANNEL,
        localCacheModel: LocalCacheModel?
    ): List<HomeLayoutResponse> {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_TOKEN, token)
            putInt(PARAM_NUM_OF_CHANNEL, numOfChannel)
            putString(PARAM_LOCATION, mapLocation(localCacheModel))
        }.parameters)

        val response = executeOnBackground().response
        return response.data
    }
}