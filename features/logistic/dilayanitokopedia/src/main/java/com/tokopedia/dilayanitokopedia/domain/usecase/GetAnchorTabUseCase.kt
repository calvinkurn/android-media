package com.tokopedia.dilayanitokopedia.home.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dilayanitokopedia.home.domain.mapper.LocationParamMapper
import com.tokopedia.dilayanitokopedia.home.domain.model.GetAnchorTabParam
import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeAnchorTabResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

/**
 * using same gql as Home Icon in HomeIconRepository
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2053540091/HPB+Home+-+API+GQL+GraphQL+getHomeIconV2
 * Created by irpan on 10/01/23.
 */
class GetAnchorTabUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetAnchorTabParam, GetHomeAnchorTabResponse>(dispatcher.io) {

    companion object {
        private const val QUERY_NAME = "DTGetHomeIconV2"
        private const val QUERY = """
            query getHomeIconV2(${'$'}param: String, ${'$'}location: String) {
              getHomeIconV2(param: ${'$'}param, location: ${'$'}location) {
                icons {
                  id
                  url
                  name
                  page
                  persona
                  brandID
                  applinks
                  imageUrl
                  buIdentifier
                  campaignCode
                  withBackground
                  categoryPersona
                  galaxyAttribution
                  feParam
                }
              }
            }

        """

        // value hardcode for dt anchor tab icons
        private const val PARAM_VALUE_PAGE_DT = "page=dt&type=anchor-icon"

        fun getParam(location: LocalCacheModel): GetAnchorTabParam {
            return GetAnchorTabParam(
                param = PARAM_VALUE_PAGE_DT,
                location = LocationParamMapper.mapLocation(location)
            )
        }
    }

    override fun graphqlQuery(): String {
        return QUERY
    }

    @GqlQuery(QUERY_NAME, QUERY)
    override suspend fun execute(params: GetAnchorTabParam): GetHomeAnchorTabResponse {
        return graphqlRepository.request(DTGetHomeIconV2(), params)
    }
}
