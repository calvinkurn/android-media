package com.tokopedia.home_recom.domain.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home_recom.domain.query.QueryPrimaryProductV2.PRIMARY_PRODUCT_V2_QUERY
import com.tokopedia.home_recom.domain.query.QueryPrimaryProductV2.PRIMARY_PRODUCT_V2_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(PRIMARY_PRODUCT_V2_QUERY_NAME, PRIMARY_PRODUCT_V2_QUERY)
internal object QueryPrimaryProductV2 {
    const val PRIMARY_PRODUCT_V2_QUERY_NAME = "PrimaryProductQueryV2"
    const val PRIMARY_PRODUCT_V2_QUERY: String = "" +
        "query primaryProductV2(${'$'}productIDString: String, ${'$'}xSource: String!, ${'$'}queryParam: String!) {\n" +
        "    productRecommendationProductDetailV2(productIDString: ${'$'}productIDString, xSource: ${'$'}xSource, queryParam: ${'$'}queryParam) {\n" +
        "        data {\n" +
        "          tID\n" +
        "          source\n" +
        "          title\n" +
        "          foreignTitle\n" +
        "          widgetUrl\n" +
        "          recommendation {\n" +
        "            status\n" +
        "            id\n" +
        "            name\n" +
        "            categoryBreadcrumbs\n" +
        "            url\n" +
        "            appUrl\n" +
        "            clickUrl\n" +
        "            wishlistUrl\n" +
        "            trackerImageUrl\n" +
        "            imageUrl\n" +
        "            price\n" +
        "            priceInt\n" +
        "            slashedPrice\n" +
        "            slashedPriceInt\n" +
        "            discountPercentage\n" +
        "            shop {\n" +
        "              id\n" +
        "              name\n" +
        "              url\n" +
        "              appUrl\n" +
        "              isGold\n" +
        "              location\n" +
        "              city\n" +
        "              reputation\n" +
        "              clover\n" +
        "              shopImage\n" +
        "            }\n" +
        "            departmentId\n" +
        "            labels {\n" +
        "              title\n" +
        "              color\n" +
        "            }\n" +
        "            badges {\n" +
        "              title\n" +
        "              imageUrl\n" +
        "            }\n" +
        "            wholesalePrice {\n" +
        "              price\n" +
        "              quantityMax\n" +
        "              quantityMin\n" +
        "              priceString\n" +
        "            }\n" +
        "            rating\n" +
        "            countReview\n" +
        "            countReviewFloat\n" +
        "            recommendationType\n" +
        "            stock\n" +
        "            minOrder\n" +
        "            isTopads\n" +
        "            isWishlist\n" +
        "            productKey\n" +
        "            shopDomain\n" +
        "            urlPath\n" +
        "          }\n" +
        "        }\n" +
        "      }\n" +
        "}\n"
}
