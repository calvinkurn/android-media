package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query

internal object GetStockReminder {

    const val QUERY = "query imsGetByProductIds(\$productIds: String!) {\n" +
            "  IMSGetByProductIDs(productIDs:\$productIds)\n" +
            "  {\n" +
            "    header {\n" +
            "      process_time\n" +
            "      reason\n" +
            "      error_code\n" +
            "    }\n" +
            "    data {\n" +
            "      product_id\n" +
            "      products_warehouse {\n" +
            "        product_id\n" +
            "        warehouse_id\n" +
            "        stock\n" +
            "        price\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "} "

}