package com.tokopedia.shop.common.data.source.cloud.query

internal object GetShopInfoTopAds {

    const val QUERY = "query ShopInfo(\$shopId: Int!){\n" +
        "  topAdsGetShopInfo(shop_id:\$shopId){\n" +
        "    data {\n" +
        "      category\n" +
        "      category_desc\n" +
        "    }\n" +
        "  }\n" +
        "}"
}