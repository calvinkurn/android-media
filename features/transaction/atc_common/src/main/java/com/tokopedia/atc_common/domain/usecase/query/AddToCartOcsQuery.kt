package com.tokopedia.atc_common.domain.usecase.query

const val ADD_TO_CART_OCS_QUERY = """
    mutation atcOCS(${'$'}params: AtcOcsParams) {
      atcOCS(params: ${'$'}params) {
        error_message
        status
        data {
          success
          message
          data {
            cart_id
            product_id
            quantity
            notes
            shop_id
            warehouse_id
            customer_id
            tracker_attribution
            tracker_list_name
            uc_ut_param
            is_trade_in
            ovo_validation {
              status
              applink
              redirect_url
              ovo_insufficient_balance {
                title
                description
                details {
                  product_price
                  shipping_estimation
                  ovo_balance
                  topup_balance
                }
                buttons {
                  ovo_topup_button {
                    text
                    applink
                    enable
                  }
                  other_methods_button {
                    text
                    applink
                    enable
                  }
                }
              }
            }
          }
          refresh_prerequisite_page
        }
      }
    }
"""
