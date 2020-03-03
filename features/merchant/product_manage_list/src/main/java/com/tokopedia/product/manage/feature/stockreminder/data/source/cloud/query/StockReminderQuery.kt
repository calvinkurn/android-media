package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query

internal object StockReminderQuery {

    const val GET_QUERY = "query imsGetByProductIds(\$productIds: String!) {\n" +
            "  IMSGetByProductIDs(productIDs:\$productIds,isSellerWH:true, additionalReqs:{Threshold:true, ShopID:true}){\n" +
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
            "        threshold\n" +
            "        shop_id\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}"

    const val CREATE_QUERY = "mutation imsCreateStockAlertThreshold(\$input: StockAlertThresholdRequest!){\n" +
            "  IMSCreateStockAlertThreshold(input: \$input){\n" +
            "    header{\n" +
            "      processTime\n" +
            "      messages\n" +
            "      reason\n" +
            "      error_code\n" +
            "    }\n" +
            "    data{\n" +
            "      product_id\n" +
            "      warehouse_id\n" +
            "      threshold    \n" +
            "    }\n" +
            "  }\n" +
            "}"

    const val UPDATE_QUERY = "mutation imsUpdateStockAlertThreshold(\$input: StockAlertThresholdRequest!){\n" +
            "  IMSUpdateStockAlertThreshold(input: \$input){\n" +
            "    header{\n" +
            "      processTime\n" +
            "      messages\n" +
            "      reason\n" +
            "      error_code\n" +
            "    }\n" +
            "    data{\n" +
            "      product_id\n" +
            "      warehouse_id\n" +
            "      threshold    \n" +
            "    }\n" +
            "  }\n" +
            "}"
}