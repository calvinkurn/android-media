package com.tokopedia.recommendation_widget_common.domain.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.recommendation_widget_common.domain.query.QueryProductRecommendationSingleV2.PRODUCT_RECOMMENDATION_SINGLE_V2_QUERY
import com.tokopedia.recommendation_widget_common.domain.query.QueryProductRecommendationSingleV2.PRODUCT_RECOMMENDATION_SINGLE_V2_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(PRODUCT_RECOMMENDATION_SINGLE_V2_QUERY_NAME, PRODUCT_RECOMMENDATION_SINGLE_V2_QUERY)
internal object QueryProductRecommendationSingleV2 {
    const val PRODUCT_RECOMMENDATION_SINGLE_V2_QUERY_NAME = "ProductRecommendationSingleQueryV2"
    const val PRODUCT_RECOMMENDATION_SINGLE_V2_QUERY: String = "" +
        "query productRecommendationSingleV2(${'$'}pageNumber: Int!, ${'$'}pageName: String!, ${'$'}productIDs: String!, ${'$'}queryParam: String!) {\n" +
        "                productRecommendationWidgetSingleV2(pageNumber: ${'$'}pageNumber, pageName: ${'$'}pageName, productIDs: ${'$'}productIDs, queryParam: ${'$'}queryParam){\n" +
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
        "                        }\n" +
        "                       }\n" +
        "                   }\n" +
        "                 }\n" +
        "             }"
}
