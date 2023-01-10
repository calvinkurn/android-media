package com.tokopedia.dilayanitokopedia.home.domain.usecase

import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeAnchorTabResponse
import com.tokopedia.dilayanitokopedia.home.domain.query.GetAnchorTabQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

/**
 * using same gql as Home Icon in HomeIconRepository
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2053540091/HPB+Home+-+API+GQL+GraphQL+getHomeIconV2
 * Created by irpan on 10/01/23.
 */
class GetHomeAnchorTabUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetHomeAnchorTabResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_TAB_NAME = "tabName"
        private const val PARAM_LOCATION = "location"
        private const val PARAM_SOURCE_TYPE = "sourceType"
        private const val PARAM_TYPE = "type"
        private const val PARAM_PRODUCT_PAGE = "productPage"
        private const val PARAM_PAGE = "page"
    }

    init {
        setGraphqlQuery(GetAnchorTabQuery)
        setTypeClass(GetHomeAnchorTabResponse::class.java)
    }

    suspend fun execute(
//        locationParamString: String,
//        page: Int
    ): GetHomeAnchorTabResponse.GetHomeIconV2 {
//        setRequestParams(
//            RequestParams.create()
//                .apply {
//                    putString(PARAM_PAGE, "dt")
//                    putString(PARAM_TYPE, "banner,position,banner_ads")
//                    putInt(PARAM_PRODUCT_PAGE, page)
//                    putString(PARAM_LOCATION, locationParamString)
//                }.parameters
//        )

        return executeOnBackground().response
    }
}
