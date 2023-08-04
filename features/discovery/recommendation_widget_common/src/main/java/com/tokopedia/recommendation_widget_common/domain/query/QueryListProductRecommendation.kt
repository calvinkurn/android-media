package com.tokopedia.recommendation_widget_common.domain.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.recommendation_widget_common.domain.query.QueryListProductRecommendation.LIST_PRODUCT_RECOMMENDATION_QUERY
import com.tokopedia.recommendation_widget_common.domain.query.QueryListProductRecommendation.LIST_PRODUCT_RECOMMENDATION_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(LIST_PRODUCT_RECOMMENDATION_QUERY_NAME, LIST_PRODUCT_RECOMMENDATION_QUERY)
object QueryListProductRecommendation {
    const val LIST_PRODUCT_RECOMMENDATION_QUERY_NAME = "ListProductRecommendationQuery"
    const val LIST_PRODUCT_RECOMMENDATION_QUERY: String = "" +
        "query productRecommendation(${'$'}userID: Int!, ${'$'}pageName: String!, ${'$'}pageNumber: Int!, ${'$'}xDevice: String!, ${'$'}xSource: String!, ${'$'}queryParam: String!, ${'$'}productIDs: String!, ${'$'}categoryIDs: String!, ${'$'}keywords: [String!]!, ${'$'}tokoNow: Boolean, ${'$'}shopIDs: String) {\n" +
        "                  productRecommendationWidget(userID: ${'$'}userID, pageName: ${'$'}pageName, pageNumber: ${'$'}pageNumber, xDevice: ${'$'}xDevice, xSource: ${'$'}xSource, queryParam: ${'$'}queryParam, productIDs : ${'$'}productIDs, categoryIDs : ${'$'}categoryIDs, keywords: ${'$'}keywords, tokoNow : ${'$'}tokoNow, shopIDs : ${'$'}shopIDs) {\n" +
        "                    data {\n" +
        "                      tID\n" +
        "                      source\n" +
        "                      title\n" +
        "                      subtitle\n" +
        "                      foreignTitle\n" +
        "                      widgetUrl\n" +
        "                      pageName\n" +
        "                      seeMoreAppLink\n" +
        "                      layoutType\n" +
        "                      pagination {\n" +
        "                        currentPage\n" +
        "                        nextPage\n" +
        "                        prevPage\n" +
        "                        hasNext\n" +
        "                      }\n" +
        "                      campaign{\n" +
        "                        appLandingPageLink\n" +
        "                        landingPageLink\n" +
        "                        thematicID\n" +
        "                        assets {\n" +
        "                          banner{\n" +
        "                            apps\n" +
        "                          }\n" +
        "                        }\n" +
        "                      }\n" +
        "                      recommendation {\n" +
        "                        id\n" +
        "                        name\n" +
        "                        categoryBreadcrumbs\n" +
        "                        url\n" +
        "                        appUrl\n" +
        "                        clickUrl\n" +
        "                        wishlistUrl\n" +
        "                        trackerImageUrl\n" +
        "                        imageUrl\n" +
        "                        price\n" +
        "                        priceInt\n" +
        "                        discountPercentage\n" +
        "                        slashedPrice\n" +
        "                        slashedPriceInt\n" +
        "                        isWishlist\n" +
        "                        minOrder\n" +
        "                        maxOrder\n" +
        "                        shop {\n" +
        "                          id\n" +
        "                          name\n" +
        "                          city\n" +
        "                          isGold\n" +
        "                          isOfficial\n" +
        "                        }\n" +
        "                        departmentId\n" +
        "                        labels {\n" +
        "                          title\n" +
        "                          color\n" +
        "                        }\n" +
        "                        labelgroup{\n" +
        "                            position\n" +
        "                            title\n" +
        "                            type\n" +
        "                            url\n" +
        "                        }\n" +
        "                        badges {\n" +
        "                          title\n" +
        "                          imageUrl\n" +
        "                        }\n" +
        "                        wholesalePrice {\n" +
        "                          price\n" +
        "                          quantityMax\n" +
        "                          quantityMin\n" +
        "                          priceString\n" +
        "                        }\n" +
        "                        freeOngkir{\n" +
        "                          isActive\n" +
        "                          imageUrl\n" +
        "                        }\n" +
        "                        rating\n" +
        "                        countReview\n" +
        "                        ratingAverage\n" +
        "                        recommendationType\n" +
        "                        stock\n" +
        "                        isTopads\n" +
        "                        specificationLabels {\n" +
        "                            key\n" +
        "                            value\n" +
        "                            type\n" +
        "                            specificationContent {\n" +
        "                               url\n" +
        "                               description\n" +
        "                            }\n" +
        "                        }\n" +
        "                        parentID\n" +
        "                        warehouseID\n"+
        "                      }\n" +
        "                    }\n" +
        "                  }\n" +
        "                }"
}
