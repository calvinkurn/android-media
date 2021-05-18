package com.tokopedia.shop.common.data.source.cloud.query

object UpdateProductStockWarehouse {

    internal const val INPUT_KEY = "input"

    internal const val QUERY = "mutation updateStockWarehouse(\$input: UpdatePWRequest!) {\n" +
            "  IMSUpdateProductWarehouse(input: \$input) {\n" +
            "    header {\n" +
            "      messages\n" +
            "      error_code\n" +
            "    }\n" +
            "    data {\n" +
            "      product_id\n" +
            "      warehouse_id\n" +
            "      stock\n" +
            "      shop_id\n" +
            "      status\n" +
            "    }\n" +
            "  }\n" +
            "}"

}