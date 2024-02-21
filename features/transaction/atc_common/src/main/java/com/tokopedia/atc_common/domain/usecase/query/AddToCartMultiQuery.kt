package com.tokopedia.atc_common.domain.usecase.query

const val ADD_TO_CART_MULTI_QUERY = """
    mutation add_to_cart_multi(${'$'}params: AddToCartMultiParam, ${'$'}chosen_address: ChosenAddressParam) {
      add_to_cart_multi(param: ${'$'}params, chosen_address: ${'$'}chosen_address) {
        error_message
        status
        data{
          success
          messages
          data {
            cart_id
            product_id
            quantity
            notes
            shop_id
          }
        }
      }
    }
"""
