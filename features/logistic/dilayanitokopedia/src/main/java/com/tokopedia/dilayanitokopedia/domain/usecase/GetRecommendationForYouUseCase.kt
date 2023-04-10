package com.tokopedia.dilayanitokopedia.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.dilayanitokopedia.domain.model.GetDtHomeRecommendationResponse
import com.tokopedia.dilayanitokopedia.domain.model.GetRecomForYouDTParam
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Get Recommendation product for list for mega tab
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2053933208/HPB+Home+-+API+GQL+GraphQL+getHomeRecommendationProductV2
 */

class GetRecommendationForYouUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetRecomForYouDTParam, GetDtHomeRecommendationResponse>(dispatcher.io) {

    companion object {
        private const val QUERY = """
        query getHomeRecommendationProductV2(${'$'}sourceType: String, ${'$'}page: String, ${'$'}type: String, ${'$'}productPage: Int, ${'$'}location: String) {
          getHomeRecommendationProductV2(sourceType: ${'$'}sourceType, page: ${'$'}page, type: ${'$'}type, productPage: ${'$'}productPage, location: ${'$'}location) {
            pageName
            hasNextPage
            products {
              id
              url
              name
              price
              rating
              applink
              clickUrl
              imageUrl
              isTopads
              priceInt
              clusterID
              productKey
              isWishlist
              wishlistUrl
              countReview
              slashedPrice
              ratingAverage
              trackerImageUrl
              slashedPriceInt
              discountPercentage
              recommendationType
              categoryBreadcrumbs
              shop {
                id
                url
                city
                name
                domain
                applink
                imageUrl
                reputation
              }
              badges {
                title
                imageUrl
              }
              freeOngkir {
                isActive
                imageUrl
              }
              labelGroup {
                url
                type
                title
                position
              }
            }
            banners {
              id
              url
              name
              target
              applink
              persona
              brandID
              imageUrl
              creativeName
              buAttribution
              categoryPersona
              galaxyAttribution
            }
            positions {
              type
            }
          }
        }
        """
        private const val QUERY_NAME = "GetDTHomeRecommendationProductV2"
        private const val VALUE_PAGE_DT = "dt"
        private const val VALUE_TYPE = "banner,position,banner_ads"
        fun getParam(productPage: Int, location: String): GetRecomForYouDTParam {
            return GetRecomForYouDTParam(
                page = VALUE_PAGE_DT,
                location = location,
                type = VALUE_TYPE,
                productPage = productPage
            )
        }
    }

    override fun graphqlQuery(): String {
        return QUERY
    }

    @GqlQuery(QUERY_NAME, QUERY)
    override suspend fun execute(params: GetRecomForYouDTParam): GetDtHomeRecommendationResponse {
        return graphqlRepository.request(GetDTHomeRecommendationProductV2(), params)
    }
}
