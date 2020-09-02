package com.tokopedia.cart.domain.usecase

fun getUpdateCartMutation(): String {
    return """
        mutation update_cart_v2(${'$'}carts: [ParamsCartUpdateCartV2Type], ${'$'}lang: String) {
            update_cart_v2(carts: ${'$'}carts, lang: ${'$'}lang) {
                error_message
                status
                data {
                    error
                    status 
                    toaster_action {
                        text
                        show_cta
                    }
                    prompt_page {
                        type
                        title
                        description
                        buttons {
                            text
                            link
                            action
                            color
                        }
                    }
                }
            }
        }
    """.trimIndent()
}