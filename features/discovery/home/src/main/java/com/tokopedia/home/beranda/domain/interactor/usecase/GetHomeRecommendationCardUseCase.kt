package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationCardMapper
import com.tokopedia.home.beranda.domain.gql.recommendationcard.GetHomeRecommendationCardResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val GET_HOME_RECOMMENDATION_CARD_QUERY = """
    query GetHomeRecommendationCard(${'$'}productPage: Int!, ${'$'}layouts: String!, ${'$'}param: String!, ${'$'}location: String!) {
        getHomeRecommendationCard(productPage: ${'$'}productPage, layouts: ${'$'}layouts, param: ${'$'}param, location: ${'$'}location) {
            pageName
            layoutName
            hasNextPage
            cards {
              id
              categoryID
              layout
              layoutTracker
              dataStringJson
              gradientColor
              url
              name
              subtitle
              price
              rating
              applink
              clickUrl
              imageUrl
              iconUrl
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
              label {
                imageUrl
                title
                textColor
                backColor
              }
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
      }
    }
"""

@GqlQuery("GetHomeRecommendationCardQuery", GET_HOME_RECOMMENDATION_CARD_QUERY)
class GetHomeRecommendationCardUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<GetHomeRecommendationCardResponse>,
    private val homeRecommendationCardMapper: HomeRecommendationCardMapper
) {

    init {
        graphqlUseCase.setGraphqlQuery(GetHomeRecommendationCardQuery())
        graphqlUseCase.setTypeClass(GetHomeRecommendationCardResponse::class.java)
    }

    suspend fun execute(
        productPage: Int,
        tabName: String,
        paramSource: String,
        location: String
    ): HomeRecommendationDataModel {
        graphqlUseCase.setRequestParams(createRequestParams(productPage, paramSource, location))
        return homeRecommendationCardMapper.mapToRecommendationCardDataModel(
            graphqlUseCase.executeOnBackground().getHomeRecommendationCard,
            tabName,
            productPage
        )
    }

    private fun createRequestParams(
        productPage: Int,
        paramSource: String,
        location: String
    ): Map<String, Any> {
        return RequestParams.create().apply {
            putInt(PARAM_PRODUCT_PAGE, productPage)
            putString(PARAM_LAYOUTS, LAYOUTS_VALUE)
            putString(PARAM_SOURCE_TYPE, paramSource)
            putString(PARAM_LOCATION, location)
        }.parameters
    }

    companion object {
        private const val PARAM_LOCATION = "location"
        private const val PARAM_SOURCE_TYPE = "param"
        private const val PARAM_PRODUCT_PAGE = "productPage"
        private const val PARAM_LAYOUTS = "layouts"

        private const val LAYOUTS_VALUE = "product,recom_card,banner_ads,video"
    }
}
