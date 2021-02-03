package com.tokopedia.shop.common.data.source.cloud.query

object AuthorizeAccess {

    const val QUERY = "query AuthorizeAccess(\$accessID: Int, \$resourceID: Int) {\n" +
            "  rbacAuthorizeAccess(accessID:\$accessID, resourceID: \$resourceID, resourceType:\"shop\"){\n" +
            "    error\n" +
            "    is_authorized\n" +
            "  }\n" +
            "}"

    const val ACCESS_ID = "accessID"
    const val RESOURCE_ID = "resourceID"

}