package com.tokopedia.atc_common.domain.usecase.query

val QUERY_ADD_TO_CART = """
            mutation add_to_cart_v2(${'$'}param: ATCV2Params) {
              add_to_cart_v2(
                param:${'$'}param) {
                    error_message
                    status
                    data {
                      success
                      cart_id
                      product_id
                      quantity
                      notes
                      shop_id
                      customer_id
                      warehouse_id
                      tracker_attribution
                      tracker_list_name
                      uc_ut_param
                      is_trade_in
                      message
                    }
                    error_reporter {
                      eligible
                      texts {
                        submit_title
                        submit_description
                        submit_button
                        cancel_button
                      }
                    }
                }
            }
        """.trimIndent()

val QUERY_ADD_TO_CART_OCC = """
    mutation add_to_cart_occ(${"$"}param : OneClickCheckoutATCParam) {
        add_to_cart_occ(param: ${"$"}param) {
            error_message
            status
            data {
                message
                success
                data {
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