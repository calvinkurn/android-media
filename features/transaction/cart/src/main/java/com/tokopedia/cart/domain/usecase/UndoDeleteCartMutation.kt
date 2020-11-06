package com.tokopedia.cart.domain.usecase

fun getUndoDeleteCartMutation(): String {
    return """
        mutation undo_remove_product_cart(${'$'}cartIds: [String], ${'$'}lang : String) {
          undo_remove_product_cart(cartIds: ${'$'}cartIds, lang: ${'$'}lang ) {
            error_message
            status
            data {
              success
              message
              data {
                cart_ids
              }
            }
          }
        }
    """
}