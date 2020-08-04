package com.tokopedia.home.benchmark.prepare_page

object TestQuery {
    val dynamicChannelQuery : String = "query getDynamicChannel(\$groupIDs: String!){\n" +
            "    dynamicHomeChannel {\n" +
            "        channels(groupIDs: \$groupIDs){\n" +
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
            "          }\n" +
            "          hero {\n" +
            "            id\n" +
            "            name\n" +
            "            url\n" +
            "            applink\n" +
            "            imageUrl\n" +
            "            attribution\n" +
            "          }\n" +
            "          grids {\n" +
            "            id\n" +
            "            name\n" +
            "            url\n" +
            "            applink\n" +
            "            price\n" +
            "            slashedPrice\n" +
            "            discount\n" +
            "            imageUrl\n" +
            "            label\n" +
            "            soldPercentage\n" +
            "            attribution\n" +
            "            productClickUrl\n" +
            "            impression\n" +
            "            cashback\n" +
            "            freeOngkir {\n" +
            "              isActive\n" +
            "              imageUrl\n" +
            "            }\n" +
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
            "\n" +
            "          }\n" +
            "        }\n" +
            "    }\n" +
            "}"

}