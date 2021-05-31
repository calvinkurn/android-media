package com.tokopedia.shop.common.data.source.cloud.query

internal object GetAdminInfoShopLocation {

    const val QUERY = "query AdminInfo(\$source: String!, \$shopId: Int!) {\n" +
        "  getAdminInfo(source: \$source, shop_id: \$shopId) {\n" +
        "    admin_data {\n" +
        "      location_list {\n" +
        "        location_id\n" +
        "        location_type\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}"
}