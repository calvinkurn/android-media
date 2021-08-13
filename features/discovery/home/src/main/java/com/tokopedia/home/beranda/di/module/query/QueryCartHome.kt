package com.tokopedia.home.beranda.di.module.query

object QueryCartHome {
    val addToCartOneClickCheckout = "mutation add_to_cart_occ(\$param: OneClickCheckoutATCParam) {\n" +
            "    add_to_cart_occ(param: \$param) {\n" +
            "        error_message\n" +
            "        status\n" +
            "        data {\n" +
            "            message\n" +
            "            success\n" +
            "            data {\n" +
            "                cart_id\n" +
            "                customer_id\n" +
            "                is_scp\n" +
            "                is_trade_in\n" +
            "                notes\n" +
            "                product_id\n" +
            "                quantity\n" +
            "                shop_id\n" +
            "                warehouse_id\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}"
}