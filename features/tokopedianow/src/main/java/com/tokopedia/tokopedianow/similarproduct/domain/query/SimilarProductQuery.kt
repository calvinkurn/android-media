package com.tokopedia.tokopedianow.similarproduct.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object SimilarProductQuery: GqlQueryInterface {

    const val PARAM_USER_ID = "userID"
    const val PARAM_PRODUCT_IDS = "productIDs"
    const val PARAM_PAGE_NAME = "pageName"

    private const val OPERATION_NAME = "productRecommendationSingle"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}${PARAM_USER_ID}: String!, ${'$'}${PARAM_PRODUCT_IDS}: String!){
            $OPERATION_NAME(${PARAM_USER_ID}:${'$'}${PARAM_USER_ID}, ${PARAM_PRODUCT_IDS}:${'$'}${PARAM_PRODUCT_IDS}, ${PARAM_PAGE_NAME}: "now_similar_page",xDevice: "android") {
                meta {
                  recommendation
                  size
                  failSize
                  processTime
                  experimentVersion
                }
                data {
                  tID
                  source
                  title
                  foreignTitle
                  widgetUrl
                  seeMoreAppLink
                  seeMoreUrlLink
                  pageName
                  recommendation {
                    id
                    name
                    categoryBreadcrumbs
                    url
                    appUrl
                    clickUrl
                    wishlistUrl
                    trackerImageUrl
                    imageUrl
                    relatedProductAppLink
                    relatedProductUrlLink
                    price
                    priceInt
                    slashedPriceInt
                    slashedPrice
                    discountPercentage
                    shop {
                      id
                      name
                      city
                    }
                    departmentId
                    labels {
                      title
                      color
                    }
                    badges {
                      title
                      imageUrl
                    }
                    wholesalePrice {
                      price
                      quantityMax
                      quantityMin
                      priceString
                    }
                    rating
                    countReview
                    recommendationType
                    stock
                    isTopads
                  }
                }
            }
       }
       """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
