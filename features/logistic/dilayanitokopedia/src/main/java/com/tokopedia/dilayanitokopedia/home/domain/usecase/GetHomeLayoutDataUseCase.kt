package com.tokopedia.dilayanitokopedia.home.domain.usecase

import com.tokopedia.dilayanitokopedia.home.domain.mapper.LocationParamMapper.mapLocation
import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.domain.query.GetHomeLayoutDataQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Dynamic Home Channel Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2043906993/HPB+Home+-+API+GQL+GraphQL+getHomeChannelV2
 */

class GetHomeLayoutDataUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetHomeLayoutResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_PAGE = "page"
        private const val PARAM_LOCATION = "location"
        private const val PARAM_VALUE_PAGE_DT = "dt"
    }

    init {
        setGraphqlQuery(GetHomeLayoutDataQuery)
        setTypeClass(GetHomeLayoutResponse::class.java)
    }

    suspend fun execute(
        localCacheModel: LocalCacheModel
    ): List<HomeLayoutResponse> {
        setRequestParams(
            RequestParams.create()
                .apply {
                    putString(PARAM_PAGE, PARAM_VALUE_PAGE_DT)
                    putString(PARAM_LOCATION, mapLocation(localCacheModel))
                }.parameters
        )

        val response = executeOnBackground().response
        return response.data
    }
}
