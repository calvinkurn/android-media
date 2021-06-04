package com.tokopedia.shop.common.constant

import com.tokopedia.shop.common.constant.GQLQueryNamedConstant.DEFAULT_SHOP_INFO_QUERY_NAME

object GqlQueryConstant {

    const val SHOP_INFO_REQUEST_QUERY_STRING = "result {\n" +
            "            shopCore{\n" +
            "                shopID\n" +
            "                description\n" +
            "                domain\n" +
            "                name\n" +
            "                tagLine\n" +
            "                url\n" +
            "            }\n" +
            "            freeOngkir{\n" +
            "                isActive\n" +
            "                imgURL\n" +
            "            }\n" +
            "            closedInfo{\n" +
            "                closedNote\n" +
            "                reason\n" +
            "                until\n" +
            "            }\n" +
            "            createInfo{\n" +
            "                openSince\n" +
            "            }\n" +
            "            shopAssets{\n" +
            "                avatar\n" +
            "                cover\n" +
            "            }\n" +
            "            shipmentInfo{\n" +
            "                isAvailable\n" +
            "                code\n" +
            "                shipmentID\n" +
            "                image\n" +
            "                name\n" +
            "                product{\n" +
            "                    isAvailable\n" +
            "                    productName\n" +
            "                    shipProdID\n" +
            "                    uiHidden\n" +
            "                }\n" +
            "                isPickup\n" +
            "                maxAddFee\n" +
            "                awbStatus\n" +
            "            }\n" +
            "            shopLastActive\n" +
            "            location\n" +
            "            isAllowManage\n" +
            "            goldOS {\n" +
            "                isGold\n" +
            "                isGoldBadge\n" +
            "                isOfficial\n" +
            "            }\n" +
            "            favoriteData{\n" +
            "                totalFavorite\n" +
            "                alreadyFavorited\n" +
            "            }\n" +
            "            statusInfo {\n" +
            "                shopStatus\n" +
            "                statusMessage\n" +
            "                statusTitle\n" +
            "            }\n" +
            "            bbInfo {\n" +
            "                bbName\n" +
            "                bbNameEN\n" +
            "                bbDesc\n" +
            "                bbDescEN\n" +
            "            }\n" +
            "            topContent{\n" +
            "                topURL\n" +
            "            }\n" +
            "            address {\n" +
            "                id\n" +
            "                name\n" +
            "                address\n" +
            "                area\n" +
            "                email\n" +
            "                phone\n" +
            "                fax\n" +
            "            }\n" +
            "            shopHomeType\n" +
            "        }"

    const val SHOP_INFO_FOR_OS_REQUEST_QUERY_STRING = "result {\n" +
            "            os{\n" +
            "                isOfficial\n" +
            "                title\n" +
            "                badge\n" +
            "                badgeSVG\n" +
            "            }\n" +
            "        }"

    const val SHOP_INFO_FOR_TOP_CONTENT_REQUEST_QUERY_STRING = "result {\n" +
            "            topContent{\n" +
            "                topURL\n" +
            "            }\n" +
            "        }"

    const val SHOP_INFO_FOR_CORE_AND_ASSETS_REQUEST_QUERY_STRING = "result {\n" +
            "            shopCore{\n" +
            "                shopID\n" +
            "                description\n" +
            "                domain\n" +
            "                name\n" +
            "                tagLine\n" +
            "                url\n" +
            "            }\n" +
            "            shopAssets{\n" +
            "                avatar\n" +
            "                cover\n" +
            "            }\n" +
            "        }"

    const val SHOP_INFO_FOR_HEADER_REQUEST_QUERY_STRING = "result {\n" +
            "                    shopCore{\n" +
            "                        shopID\n" +
            "                        description\n" +
            "                        domain\n" +
            "                        name\n" +
            "                        tagLine\n" +
            "                        url\n" +
            "                    }\n" +
            "                    freeOngkir{\n" +
            "                        isActive\n" +
            "                        imgURL\n" +
            "                    }\n" +
            "                    closedInfo{\n" +
            "                        closedNote\n" +
            "                        reason\n" +
            "                        until\n" +
            "                    }\n" +
            "                    createInfo{\n" +
            "                        openSince\n" +
            "                    }\n" +
            "                    shopAssets{\n" +
            "                        avatar\n" +
            "                        cover\n" +
            "                    }\n" +
            "                    shopLastActive\n" +
            "                    location\n" +
            "                    isAllowManage\n" +
            "                    shopSnippetURL\n" +
            "                    statusInfo {\n" +
            "                        shopStatus\n" +
            "                        statusMessage\n" +
            "                        statusTitle\n" +
            "                    }\n" +
            "                }"

    const val SHOP_REPUTATION_QUERY_STRING = "query getShopBadge(\$shopIds: [Int!]!){\n" +
            "     reputation_shops(shop_ids: \$shopIds) {\n" +
            "         badge\n" +
            "         badge_hd\n" +
            "         score\n" +
            "         score_map\n" +
            "     }\n" +
            " }"

    const val GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS_QUERY_STRING = "query getShopOperationalHourStatus(\$shopID: String!, \$type:Int!) {\n" +
            "    getShopOperationalHourStatus(shopID: \$shopID, type: \$type) {\n" +
            "        timestamp\n" +
            "      \tstatusActive\n" +
            "      \tstartTime\n" +
            "\t\tendTime\n" +
            "      \terror{\n" +
            "          message\n" +
            "        }\n" +
            "        tickerTitle\n" +
            "        tickerMessage\n" +
            "    }\n" +
            "}"

    const val QUERY_SHOP_SCORE_STRING = "query getShopScore(\$shopId: String!) {\n" +
            "  shopScore(input: {shopID: \$shopId}) {\n" +
            "    result {\n" +
            "      shopID\n" +
            "      shopScore\n" +
            "      shopScoreSummary {\n" +
            "        title\n" +
            "        value\n" +
            "        maxValue\n" +
            "        color\n" +
            "        description\n" +
            "      }\n" +
            "      badgeScore\n" +
            "    }\n" +
            "  }\n" +
            "}"

    const val FAVORITE_STATUS_GQL_STRING = "result {\n" +
            "             favoriteData{\n" +
            "                 totalFavorite\n" +
            "                 alreadyFavorited\n" +
            "             }\n" +
            "         }"

    const val SHOP_CLOSE_DETAIL_INFO_QUERY_STRING = "result {\n" +
            "      statusInfo {\n" +
            "        shopStatus\n" +
            "      }\n" +
            "      closedInfo {\n" +
            "        detail {\n" +
            "          startDate\n" +
            "          endDate\n" +
            "          openDate\n" +
            "          status\n" +
            "          startDateUTC\n" +
            "          endDateUTC\n" +
            "          openDateUTC\n" +
            "        }\n" +
            "        closedNote\n" +
            "        reason\n" +
            "        until\n" +
            "      }\n" +
            "    }"

    private const val SHOP_INFO_BASE_QUERY_STRING = "query %1s(\$shopIds: [Int!]!, \$fields: [String!]!, \$shopDomain: String, \$source: String){\n" +
            "    shopInfoByID(input: {\n" +
            "        shopIDs: \$shopIds,\n" +
            "        fields: \$fields,\n" +
            "        domain: \$shopDomain,\n" +
            "        source: \$source\n" +
            "    }){\n" +
            "%2s" +
            "        \n" +
            "    }\n" +
            "}"

    fun getShopInfoQuery(requestQuery: String, queryName: String = DEFAULT_SHOP_INFO_QUERY_NAME): String {
        return String.format(SHOP_INFO_BASE_QUERY_STRING, queryName, requestQuery)
    }
}