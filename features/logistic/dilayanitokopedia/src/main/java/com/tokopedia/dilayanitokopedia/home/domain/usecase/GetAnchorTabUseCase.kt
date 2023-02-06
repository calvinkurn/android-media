package com.tokopedia.dilayanitokopedia.home.domain.usecase

import com.tokopedia.dilayanitokopedia.home.domain.mapper.LocationParamMapper
import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeAnchorTabResponse
import com.tokopedia.dilayanitokopedia.home.domain.query.GetAnchorTabQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * using same gql as Home Icon in HomeIconRepository
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2053540091/HPB+Home+-+API+GQL+GraphQL+getHomeIconV2
 * Created by irpan on 10/01/23.
 */
class GetAnchorTabUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetHomeAnchorTabResponse>(graphqlRepository) {

    companion object {
        private const val PARAM = "param"
        private const val PARAM_LOCATION = "location"

        // value hardcode for dt anchor tab icons
        private const val PARAM_VALUE_PAGE_DT = "page=dt&type=anchor-icon"
    }

    init {
        setGraphqlQuery(GetAnchorTabQuery)
        setTypeClass(GetHomeAnchorTabResponse::class.java)
    }

    suspend fun execute(
        localCacheModel: LocalCacheModel
    ): GetHomeAnchorTabResponse.GetHomeIconV2 {
        setRequestParams(
            RequestParams.create()
                .apply {
                    putString(PARAM, PARAM_VALUE_PAGE_DT)
                    putString(PARAM_LOCATION, LocationParamMapper.mapLocation(localCacheModel))
                }.parameters
        )

        return executeOnBackground().response
    }
}
