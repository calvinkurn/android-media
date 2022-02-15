package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryDynamicChannel.DYNAMIC_CHANNEL_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryDynamicChannel.DYNAMIC_CHANNEL_QUERY_NAME

@GqlQuery(DYNAMIC_CHANNEL_QUERY_NAME, DYNAMIC_CHANNEL_QUERY)
internal object QueryDynamicChannel {
    const val DYNAMIC_CHANNEL_QUERY_NAME = "DynamicChannelQuery"
    const val DYNAMIC_CHANNEL_QUERY: String = "query getDynamicChannel(\$groupIDs: String!, \$numOfChannel: Int!, \$token: String!, \$param: String!, \$location: String){\n" +
            "    dynamicHomeChannel {\n" +
            "        channels(groupIDs: \$groupIDs, numOfChannel: \$numOfChannel, token: \$token, param: \$param, location: \$location){\n" +
            "          id\n" +
            "          dividerType\n" +
            "          group_id\n" +
            "          galaxy_attribution\n" +
            "          persona\n" +
            "          brand_id\n" +
            "          category_persona\n" +
            "          name\n" +
            "          layout\n" +
            "          type\n" +
            "          pageName\n" +
            "          showPromoBadge\n" +
            "          categoryID\n" +
            "          perso_type\n" +
            "          campaignCode\n" +
            "          has_close_button\n" +
            "          isAutoRefreshAfterExpired\n" +
            "          token\n" +
            "          widgetParam\n" +
            "          contextualInfo\n" +
            "          viewAllCard {\n" +
            "              id\n" +
            "              contentType\n" +
            "              title\n" +
            "              description\n" +
            "              imageUrl\n" +
            "              gradientColor\n" +
            "          }\n" +
            "          header {\n" +
            "            id\n" +
            "            name\n" +
            "            subtitle\n" +
            "            url\n" +
            "            applink\n" +
            "            serverTime\n" +
            "            expiredTime\n" +
            "            backColor\n" +
            "            backImage\n" +
            "            textColor\n" +
            "          }\n" +
            "           grids {\n" +
            "             campaignCode\n" +
            "             id\n" +
            "             back_color\n" +
            "             name\n" +
            "             url\n" +
            "             applink\n" +
            "             price\n" +
            "             slashedPrice\n" +
            "             discount\n" +
            "             imageUrl\n" +
            "             label\n" +
            "             soldPercentage\n" +
            "             attribution\n" +
            "             productClickUrl\n" +
            "             impression\n" +
            "             cashback\n" +
            "             isTopads\n" +
            "             freeOngkir {\n" +
            "                isActive\n" +
            "                imageUrl\n" +
            "              }\n" +
            "              productViewCountFormatted\n" +
            "              isOutOfStock\n" +
            "              warehouseID\n" +
            "              minOrder\n" +
            "              recommendationType\n" +
            "              shop{\n" +
            "                shopID\n" +
            "                city\n" +
            "                name\n" +
            "                applink\n" +
            "               }\n" +
            "              labelGroup {\n" +
            "                title\n" +
            "                position\n" +
            "                type\n" +
            "                url\n" +
            "              }\n" +
            "              has_buy_button\n" +
            "              rating\n" +
            "              ratingAverage\n" +
            "              count_review\n" +
            "              benefit {\n" +
            "                 type\n" +
            "                 value\n" +
            "              }\n" +
            "              textColor\n" +
            "              badges {\n" +
            "                 title\n" +
            "                 image_url\n" +
            "              }\n" +
            "          }\n" +
            "          banner {\n" +
            "            id\n" +
            "            title\n" +
            "            description\n" +
            "            url\n" +
            "            back_color\n" +
            "            cta {\n" +
            "              type\n" +
            "              mode\n" +
            "              text\n" +
            "              coupon_code\n" +
            "            }\n" +
            "            applink\n" +
            "            text_color\n" +
            "            image_url\n" +
            "            attribution\n" +
            "            gradient_color\n" +
            "\n" +
            "          }\n" +
            "        }\n" +
            "    }\n" +
            "}"
}