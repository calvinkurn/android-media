package com.tokopedia.atc_common.domain.usecase.query

val QUERY_ADD_TO_CART_OCC_MULTI = """
    mutation add_to_cart_occ_multi(${"$"}param : OneClickCheckoutMultiATCParam) {
        add_to_cart_occ_multi(param: ${"$"}param) {
            error_message
            status
            data {
                message
                success
                carts {
                    cart_id
                    customer_id
                    is_scp
                    is_trade_in
                    notes
                    product_id
                    quantity
                    shop_id
                    warehouse_id
                }
            }
        }
    }
""".trimIndent()

val QUERY_ADD_TO_CART_OCC_EXTERNAL_MULTI = """
    mutation add_to_cart_occ_external_multi(${"$"}param : OneClickCheckoutATCParam) {
        add_to_cart_occ_external_multi(param: ${"$"}param) {
            error_message
            status
            data {
                message
                success
                carts {
                    product_id
                    product_name
                    quantity
                    price
                    category
                    shop_id
                    shop_type
                    shop_name
                    picture
                    url
                    cart_id
                    brand
                    category_id
                    variant
                    tracker_attribution
                    is_multi_origin
                    is_free_ongkir
                    is_free_ongkir_extra
                }
            }
        }
    }
""".trimIndent()