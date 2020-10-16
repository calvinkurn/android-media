package com.tokopedia.cart.domain.usecase

fun getDeleteCartMutation(): String {
    return """
        mutation remove_from_cart(${'$'}addWishlist: Int, ${'$'}cartIds: [String], ${'$'}lang: String){
            remove_from_cart(addWishlist: ${'$'}addWishlist, cartIds:${'$'}cartIds, lang: ${'$'}lang){
            error_message
            status
            data {
                message
                success
            }
          }
        }
    """
}