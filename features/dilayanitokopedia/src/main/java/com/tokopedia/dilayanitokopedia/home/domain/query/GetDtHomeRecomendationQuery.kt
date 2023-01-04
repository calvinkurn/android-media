package com.tokopedia.dilayanitokopedia.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by irpan on 29/11/22.
 */

internal object GetDtHomeRecomendationQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "getHomeRecommendationProductV2"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}sourceType: String, ${'$'}page: String, ${'$'}type: String, ${'$'}productPage: Int, ${'$'}location: String) {
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
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
