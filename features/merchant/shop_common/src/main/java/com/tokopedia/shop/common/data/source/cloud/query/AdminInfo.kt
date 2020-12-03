package com.tokopedia.shop.common.data.source.cloud.query

internal object AdminInfo {

    val QUERY = "query AdminInfo(\$source: String!, \$shopId: Int!) {\n" +
            "  getAdminInfo(source: \$source, shop_id: \$shopId) {\n" +
            "    admin_data {\n" +
            "      permission_list {\n" +
            "        permission_id\n" +
            "      }\n" +
            "      detail_information {\n" +
            "        admin_role_type {\n" +
            "          is_shop_admin\n" +
            "          is_location_admin\n" +
            "          is_shop_owner\n" +
            "        }\n" +
            "      }\n" +
            "      response_detail {\n" +
            "        code\n" +
            "        error_message\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}"

}