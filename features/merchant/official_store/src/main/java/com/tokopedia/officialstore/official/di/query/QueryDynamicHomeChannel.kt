package com.tokopedia.officialstore.official.di.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.officialstore.official.di.query.QueryDynamicHomeChannel.DYNAMIC_HOME_CHANNEL_QUERY
import com.tokopedia.officialstore.official.di.query.QueryDynamicHomeChannel.DYNAMIC_HOME_CHANNEL_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(DYNAMIC_HOME_CHANNEL_QUERY_NAME, DYNAMIC_HOME_CHANNEL_QUERY)
internal object QueryDynamicHomeChannel {
    const val DYNAMIC_HOME_CHANNEL_QUERY_NAME = "DynamicHomeChannelQuery"
    const val DYNAMIC_HOME_CHANNEL_QUERY =
        "query dynamicHomeChannel(\$type: String, \$location: String) {\n" +
                "    dynamicHomeChannel {\n" +
                "        channels(type: \$type, location: \$location) {\n" +
                "            id\n" +
                "            name\n" +
                "            layout\n" +
                "            pageName\n" +
                "            type\n" +
                "            campaignID\n" +
                "            widgetParam\n" +
                "            contextualInfo\n" +
                "            campaignCode\n" +
                "            viewAllCard {\n" +
                "                id\n" +
                "                contentType\n" +
                "                title\n" +
                "                description\n" +
                "                imageUrl\n" +
                "                gradientColor\n" +
                "            }\n" +
                "            header {\n" +
                "                id\n" +
                "                name\n" +
                "                url\n" +
                "                applink\n" +
                "                serverTime\n" +
                "                expiredTime\n" +
                "                backColor\n" +
                "                backImage\n" +
                "                subtitle\n" +
                "                textColor\n" +
                "            }\n" +
                "            hero {\n" +
                "                id\n" +
                "                name\n" +
                "                url\n" +
                "                applink\n" +
                "                imageUrl\n" +
                "                attribution\n" +
                "            }\n" +
                "            grids {\n" +
                "                id\n" +
                "                name\n" +
                "                url\n" +
                "                applink\n" +
                "                price\n" +
                "                slashedPrice\n" +
                "                discount\n" +
                "                imageUrl\n" +
                "                label\n" +
                "                soldPercentage\n" +
                "                attribution\n" +
                "                productClickUrl\n" +
                "                impression\n" +
                "                cashback\n" +
                "                rating\n" +
                "                ratingAverage\n" +
                "                count_review\n" +
                "                warehouseID\n" +
                "                discountPercentage\n" +
                "                freeOngkir {\n" +
                "                    isActive\n" +
                "                    imageUrl\n" +
                "                }\n" +
                "                shop{\n" +
                "                    shopID\n" +
                "                    name\n" +
                "                    applink\n" +
                "                    city\n" +
                "                    imageUrl\n" +
                "                    url\n" +
                "                }\n" +
                "                campaignCode\n" +
                "                labelGroup {\n" +
                "                    position\n" +
                "                    title\n" +
                "                    type\n" +
                "                    url\n" +
                "                }\n" +
                "                back_color\n" +
                "                productImageUrl\n" +
                "                benefit{\n" +
                "                    type\n" +
                "                    value\n" +
                "                }\n" +
                "                badges{\n" +
                "                    image_url\n" +
                "                }\n" +
                "                expiredTime\n" +
                "            }\n" +
                "            banner {\n" +
                "                id\n" +
                "                title\n" +
                "                description\n" +
                "                url\n" +
                "                cta {\n" +
                "                    type\n" +
                "                    mode\n" +
                "                    text\n" +
                "                    coupon_code\n" +
                "                }\n" +
                "                applink\n" +
                "                text_color\n" +
                "                image_url\n" +
                "                back_color\n" +
                "                gradient_color\n" +
                "                attribution\n" +
                "            }\n" +
                "            galaxy_attribution\n" +
                "\t        persona\n" +
                "       \t    category_persona\n" +
                "       \t    brand_id\n" +
                "        }\n" +
                "    }\n" +
                "}"
}