package com.tokopedia.recommendation_widget_common.domain.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.recommendation_widget_common.domain.query.QueryProductRecommendationSingle.PRODUCT_RECOMMENDATION_SINGLE_QUERY
import com.tokopedia.recommendation_widget_common.domain.query.QueryProductRecommendationSingle.PRODUCT_RECOMMENDATION_SINGLE_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(PRODUCT_RECOMMENDATION_SINGLE_QUERY_NAME, PRODUCT_RECOMMENDATION_SINGLE_QUERY)
internal object QueryProductRecommendationSingle {
    const val PRODUCT_RECOMMENDATION_SINGLE_QUERY_NAME = "ProductRecommendationSingleQuery"
    const val PRODUCT_RECOMMENDATION_SINGLE_QUERY: String = "" +
        "query productRecommendationSingle(${'$'}pageNumber: Int!, ${'$'}pageName: String!, ${'$'}productIDs: String!, ${'$'}queryParam: String!, ${'$'}productCardVersion: Int, ${'$'}currentSessionID: String!, ${'$'}refreshType: String!) {\n" +
        "                productRecommendationWidgetSingle(pageNumber: ${'$'}pageNumber, pageName: ${'$'}pageName, productIDs: ${'$'}productIDs, queryParam: ${'$'}queryParam, productCardVersion : ${'$'}productCardVersion, currentSessionID: ${'$'}currentSessionID, refreshType: ${'$'}refreshType){\n" +
        "                   data {\n" +
        "                       tID\n" +
        "                       source\n" +
        "                       title\n" +
        "                       foreignTitle\n" +
        "                       widgetUrl\n" +
        "                       seeMoreAppLink\n" +
        "                       seeMoreUrlLink\n" +
        "                       pageName\n" +
        "                       pagination{\n" +
        "                            hasNext\n" +
        "                       }\n" +
        "                       campaign{\n" +
        "                        appLandingPageLink\n" +
        "                        landingPageLink\n" +
        "                        assets {\n" +
        "                          banner{\n" +
        "                            apps\n" +
        "                          }\n" +
        "                        }\n" +
        "                       }\n" +
        "                       appLog {\n" +
        "                         sessionID\n" +
        "                         requestID\n" +
        "                         logID\n" +
        "                       }\n" +
        "                       recommendation {\n" +
        "                           id\n" +
        "                           name\n" +
        "                           categoryBreadcrumbs\n" +
        "                           url\n" +
        "                           appUrl\n" +
        "                           clickUrl\n" +
        "                           wishlistUrl\n" +
        "                           trackerImageUrl\n" +
        "                           imageUrl\n" +
        "                           relatedProductAppLink\n" +
        "                           relatedProductUrlLink\n" +
        "                           price\n" +
        "                           priceInt\n" +
        "                           discountPercentage\n" +
        "                           slashedPrice\n" +
        "                           slashedPriceInt\n" +
        "                           minOrder\n" +
        "                           warehouseID\n" +
        "                           shop {\n" +
        "                               id\n" +
        "                               name\n" +
        "                               city\n" +
        "                           }\n" +
        "                          departmentId\n" +
        "                          labels {\n" +
        "                              title\n" +
        "                              color\n" +
        "                          }\n" +
        "                          labelgroup{\n" +
        "                            position\n" +
        "                            title\n" +
        "                            type\n" +
        "                            url\n" +
        "                            styles{\n" +
        "                               key\n" +
        "                               value\n" +
        "                            }\n" +
        "                          }\n" +
        "                          badges {\n" +
        "                              title\n" +
        "                              imageUrl\n" +
        "                          }\n" +
        "                          wholesalePrice {\n" +
        "                              price\n" +
        "                              quantityMax\n" +
        "                              quantityMin\n" +
        "                              priceString\n" +
        "                          }\n" +
        "                          freeOngkir{\n" +
        "                            isActive\n" +
        "                            imageUrl\n" +
        "                          }\n" +
        "                          rating\n" +
        "                          ratingAverage\n" +
        "                          countReview\n" +
        "                          recommendationType\n" +
        "                          stock\n" +
        "                          isTopads\n" +
        "                          specificationLabels {\n" +
        "                            key\n" +
        "                            value\n" +
        "                          }\n" +
        "                          recParam\n" +
        "                          adsLog {\n"+
        "                            creativeID\n" +
        "                            logExtra\n" +
        "                          }\n" +
        "                          parentID\n" +
        "                       }\n" +
        "                   }\n" +
        "                 }\n" +
        "             }"
}
