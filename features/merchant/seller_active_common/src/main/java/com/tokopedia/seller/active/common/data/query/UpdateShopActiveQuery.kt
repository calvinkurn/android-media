package com.tokopedia.seller.active.common.data.query

object UpdateShopActiveQuery {
    const val QUERY = "mutation updateShopActive(\$input: ParamUpdateLastActive!){\n" +
            "  updateShopActive(input: \$input) {\n" +
            "    success\n" +
            "    message\n" +
            "  }\n" +
            "  status\n" + //Don't remove `status` field since it's necessary for refresh token flow
            "}"
}