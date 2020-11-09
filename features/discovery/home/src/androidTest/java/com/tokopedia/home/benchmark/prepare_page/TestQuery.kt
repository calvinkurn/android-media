package com.tokopedia.home.benchmark.prepare_page

object TestQuery {
    val dynamicChannelQuery : String = "query getDynamicChannel(\$groupIDs: String!, \$numOfChannel: Int!, \$token: String!){\n" +
            "    dynamicHomeChannel {\n" +
            "        channels(groupIDs: \$groupIDs, numOfChannel: \$numOfChannel, token: \$token){\n" +
            "          id\n" +
            "          group_id\n" +
            "          galaxy_attribution\n" +
            "          persona\n" +
            "          brand_id\n" +
            "          category_persona\n" +
            "          name\n" +
            "          layout\n" +
            "          type\n" +
            "          showPromoBadge\n" +
            "          categoryID\n" +
            "          perso_type\n" +
            "          campaignCode\n" +
            "          has_close_button\n" +
            "          isAutoRefreshAfterExpired\n" +
            "          token\n" +
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
            "               }\n" +
            "              labelGroup {\n" +
            "                title\n" +
            "                position\n" +
            "                type\n" +
            "              }\n" +
            "              has_buy_button\n" +
            "              rating\n" +
            "              count_review\n" +
            "              benefit {\n" +
            "                 type\n" +
            "                 value\n" +
            "              }\n" +
            "              textColor\n" +
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

    val homeQuery: String = "" +
            "query homeData\n" +
            "        {\n" +
            "        status\n" +
            "          ticker {\n" +
            "            meta {\n" +
            "              total_data\n" +
            "            }\n" +
            "            tickers\n" +
            "            {\n" +
            "              id\n" +
            "              title\n" +
            "              message\n" +
            "              color\n" +
            "              layout\n" +
            "              ticker_type\n" +
            "              title\n" +
            "            }\n" +
            "          }\n" +
            "          slides(device: 32) {\n" +
            "            meta { total_data }\n" +
            "            slides {\n" +
            "              id\n" +
            "              galaxy_attribution\n" +
            "              persona\n" +
            "              brand_id\n" +
            "              category_persona\n" +
            "              image_url\n" +
            "              redirect_url\n" +
            "              applink\n" +
            "              topads_view_url\n" +
            "              promo_code\n" +
            "              creative_name\n" +
            "              type\n" +
            "              category_id\n" +
            "              campaignCode\n" +
            "            }\n" +
            "          }\n" +
            "          dynamicHomeIcon {\n" +
            "            dynamicIcon {\n" +
            "              id\n" +
            "              galaxy_attribution\n" +
            "              persona\n" +
            "              brand_id\n" +
            "              category_persona\n" +
            "              name\n" +
            "              url\n" +
            "              imageUrl\n" +
            "              applinks\n" +
            "              bu_identifier\n" +
            "            }\n" +
            "          }\n" +
            "          homeFlag{\n" +
            "                event_time\n" +
            "                server_time\n" +
            "                flags(name: \"has_recom_nav_button,dynamic_icon_wrap,has_tokopoints,is_autorefresh\"){\n" +
            "                    name\n" +
            "                    is_active\n" +
            "                }\n" +
            "            }\n" +
            "        }"

}