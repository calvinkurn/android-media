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
                      is_fulfillment
                      add_ons {
                         id
                         unique_id
                         status
                      }
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

val MUTATION_ADD_TO_CART_BUNDLE = """
    mutation add_to_cart_bundle(${'$'}params: AddToCartBundleParam, ${'$'}chosen_address: ChosenAddressParam, ${'$'}dummy: Int) {
      add_to_cart_bundle(params: ${'$'}params, chosen_address: ${'$'}chosen_address, dummy: ${'$'}dummy) {
        error_message
        status
        data{
          success
          messages
          data{
            cart_id
            product_id
            quantity
            notes
            shop_id
          }
        }
      }
    }
""".trimIndent()
