package com.tokopedia.shop.common.data.source.cloud.query

object AdminPermission {

    const val QUERY = "query AdminType() {\n" +
            "  getAdminType(source: \"akw-testing\"){\n" +
            "    admin_data {\n" +
            "      permission_list {\n" +
            "        permission_id\n" +
            "      }\n" +
            "    }\n" +
            "    response_detail {\n" +
            "      error_message\n" +
            "    }\n" +
            "  }\n" +
            "}"

}