package com.tokopedia.atc_common.domain.usecase.query

const val UPDATE_CART_COUNTER_MUTATION = """
    mutation update_cart_counter{
      update_cart_counter(){
            count
      }
    }
"""
