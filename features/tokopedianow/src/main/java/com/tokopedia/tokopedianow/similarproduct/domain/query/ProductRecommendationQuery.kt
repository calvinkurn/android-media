package com.tokopedia.tokopedianow.similarproduct.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object ProductRecommendationQuery: GqlQueryInterface {

    const val PARAM_USER_ID = "userID"
    const val PARAM_PRODUCT_IDS = "productIDs"
    const val PARAM_PAGE_NAME = "pageName"
    const val PARAM_QUERY = "queryParam"
    const val PARAM_X_DEVICE = "xDevice"
    const val PARAM_TOKONOW = "tokoNow"

    private const val OPERATION_NAME = "productRecommendationWidgetSingle"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}$PARAM_USER_ID: Int!, ${'$'}$PARAM_PRODUCT_IDS: String!, ${'$'}$PARAM_QUERY: String!){
            $OPERATION_NAME($PARAM_USER_ID:${'$'}$PARAM_USER_ID, $PARAM_PRODUCT_IDS:${'$'}$PARAM_PRODUCT_IDS, $PARAM_QUERY:${'$'}$PARAM_QUERY, $PARAM_PAGE_NAME: "now_similar_page", $PARAM_X_DEVICE: "android", $PARAM_TOKONOW: true) {
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
                    parentID
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
                    warehouse_ids
                    stock
                    minOrder
                    maxOrder
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
