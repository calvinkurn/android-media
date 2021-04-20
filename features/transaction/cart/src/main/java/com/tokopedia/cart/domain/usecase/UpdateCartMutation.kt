package com.tokopedia.cart.domain.usecase

fun getUpdateCartMutation(): String {
    return """
        mutation update_cart_v2(${'$'}carts: [ParamsCartUpdateCartV2Type], ${'$'}lang: String,  ${'$'}chosen_address: ChosenAddressParam) {
            update_cart_v2(carts: ${'$'}carts, lang: ${'$'}lang, chosen_address: ${'$'}chosen_address) {
                error_message
                status
                data {
                    error
                    status 
                    toaster_action {
                        text
                        show_cta
                    }
                    out_of_service {
                        id
                        code
                        image
                        title
                        description
                        buttons {
                          id
                          code
                          message
                          color
                        }
                    }
                }
            }
        }
    """
}