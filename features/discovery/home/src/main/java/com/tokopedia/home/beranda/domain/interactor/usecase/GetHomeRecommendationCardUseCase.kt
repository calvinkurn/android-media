package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationCardMapper
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.gql.recommendationcard.GetHomeRecommendationCardResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.productcard.experiments.ProductCardExperiment
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val GET_HOME_RECOMMENDATION_CARD_QUERY = """
    query GetHomeRecommendationCard(${'$'}productPage: Int!, ${'$'}layouts: String!, ${'$'}param: String!, ${'$'}location: String!, , ${'$'}productCardVersion: String!) {
        getHomeRecommendationCard(productPage: ${'$'}productPage, layouts: ${'$'}layouts, param: ${'$'}param, location: ${'$'}location, productCardVersion: ${'$'}productCardVersion) {
            pageName
            layoutName
            hasNextPage
            appLog {
                bytedanceSessionID
                requestID
                logID
            }
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
                styles {
                 key
                 value
                }
              }
              recParam
            }
      }
    }
"""

@HomeScope
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
        location: String,
        refreshType: Int,
        bytedanceSessionId: String,
    ): HomeRecommendationDataModel {
        graphqlUseCase.setRequestParams(
            createRequestParams(
                productPage,
                paramSource,
                location,
                refreshType,
                bytedanceSessionId
            )
        )
        return homeRecommendationCardMapper.mapToRecommendationCardDataModel(
            graphqlUseCase.executeOnBackground().getHomeRecommendationCard,
            tabName,
            productPage
        )
    }

    private fun createRequestParams(
        productPage: Int,
        paramSource: String,
        location: String,
        refreshType: Int,
        bytedanceSessionId: String,
    ): Map<String, Any> {
        return RequestParams.create().apply {
            putInt(PARAM_PRODUCT_PAGE, productPage)
            putString(PARAM_LAYOUTS, LAYOUTS_VALUE)
            putString(PARAM_SOURCE_TYPE, paramSource)
            putString(PARAM_LOCATION, location)
            putString(PRODUCT_CARD_VERSION, getProductCardVersion())
            putInt(REFRESH_TYPE, refreshType)
            putString(BYTEDANCE_SESSION_ID, bytedanceSessionId)
        }.parameters
    }

    private fun getProductCardVersion(): String {
        val isReimagine = ProductCardExperiment.isReimagine()
        return if(isReimagine) PRODUCT_CARD_VERSION_V5 else ""
    }

    companion object {
        private const val PARAM_LOCATION = "location"
        private const val PARAM_SOURCE_TYPE = "param"
        private const val PARAM_PRODUCT_PAGE = "productPage"
        private const val PARAM_LAYOUTS = "layouts"
        private const val PRODUCT_CARD_VERSION = "productCardVersion"
        private const val PRODUCT_CARD_VERSION_V5 = "v5"
        private const val REFRESH_TYPE = "refreshType"
        private const val BYTEDANCE_SESSION_ID = "bytedanceSessionID"
        const val REFRESH_TYPE_UNKNOWN = -1
        const val REFRESH_TYPE_OPEN = 0
        const val REFRESH_TYPE_REFRESH = 1
        const val REFRESH_TYPE_LOAD_MORE = 2
        const val REFRESH_TYPE_PUSH = 3

        private const val LAYOUTS_VALUE = "product,recom_card,banner_ads,video"
    }
}
