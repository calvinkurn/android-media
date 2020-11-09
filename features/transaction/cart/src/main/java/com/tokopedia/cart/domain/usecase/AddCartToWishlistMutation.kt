package com.tokopedia.cart.domain.usecase

fun getAddToWishlistMutation(): String {
    return """
        mutation add_to_wishlist_v2(${'$'}params: AddWishlistCartRevampParams) {
          add_to_wishlist_v2(params: ${'$'}params) {
            error_message
            status
            data {
              success
              messages
            }
          }
        }
    """
}