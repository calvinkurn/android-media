package com.tokopedia.home.beranda.di.module.query

object QueryHome {
    val dynamicChannelQuery : String = "query getDynamicChannel(\$groupIDs: String!, \$numOfChannel: Int!, \$token: String!, \$param: String!, \$location: String){\n" +
            "    dynamicHomeChannel {\n" +
            "        channels(groupIDs: \$groupIDs, numOfChannel: \$numOfChannel, token: \$token, param: \$param, location: \$location){\n" +
            "          id\n" +
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
            "              campaignCode\n" +
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

    val homeDataRevampQuery: String = "" +
            "query homeData\n" +
            "        {\n" +
            "        status\n" +
            "          homeFlag{\n" +
            "                event_time\n" +
            "                server_time\n" +
            "                flags(name: \"has_recom_nav_button,dynamic_icon_wrap,has_tokopoints,is_autorefresh\"){\n" +
            "                    name\n" +
            "                    is_active\n" +
            "                    integer_value\n" +
            "                }\n" +
            "            }\n" +
            "        }"

    val homeTickerQuery: String = "" +
            "query homeTicker(\$location: String)\n" +
            "        {\n" +
            "          ticker {\n" +
            "            meta {\n" +
            "              total_data\n" +
            "            }\n" +
            "            tickers(location: \$location)\n" +
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
            "        }"

    val homeSlidesQuery: String = "" +
            "query homeSlides\n" +
            "        {\n" +
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
            "        }"

    val homeIconQuery: String = "" +
            "query homeIcon(\$param: String, \$location: String)\n" +
            "        {\n" +
            "          dynamicHomeIcon {\n" +
            "            dynamicIcon(param: \$param, location: \$location) {\n" +
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
            "              campaignCode\n" +
            "              withBackground\n" +
            "            }\n" +
            "          }\n" +
            "        }"

    val recommendationQuery : String =
            "query getRecommendation(\$location: String)\n" +
            " {\n" +
            "  get_home_recommendation(location: \$location){\n" +
            "    recommendation_tabs{\n" +
            "      id\n" +
            "      name\n" +
            "      image_url\n" +
            "    }\n" +
            "  }\n" +
            " }"

    val closeChannel = "mutation closeChannel(\$channelID: Int!){\n" +
            "  close_channel(channelID: \$channelID){\n" +
            "    success\n" +
            "    message\n" +
            "  }\n" +
            "}"

    val atfQuery = "query getAtf {\n" +
            "  dynamicPosition{\n" +
            "    id\n" +
            "    name\n" +
            "    component\n" +
            "    param\n" +
            "    isOptional\n" +
            "    }\n" +
            "}"
}