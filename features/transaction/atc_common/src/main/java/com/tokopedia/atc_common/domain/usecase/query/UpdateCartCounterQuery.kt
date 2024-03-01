package com.tokopedia.atc_common.domain.usecase.query

const val UPDATE_CART_COUNTER_QUERY = """
    mutation updateCartCounter() {
      update_cart_counter() {
            count
      }
  }
"""
