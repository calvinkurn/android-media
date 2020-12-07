package com.tokopedia.shop.common.data.source.cloud.query

object AdminPermissionList {

    val QUERY = "query AdminPermission(\$source: String!, \$shopId: Int!) {\n" +
            "  getAdminInfo(source: \$source, shop_id: \$shopId) {\n" +
            "    admin_data {\n" +
            "      permission_list {\n" +
            "        permission_id\n" +
            "      }\n" +
            "      response_detail {\n" +
            "        code\n" +
            "        error_message\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}"
}