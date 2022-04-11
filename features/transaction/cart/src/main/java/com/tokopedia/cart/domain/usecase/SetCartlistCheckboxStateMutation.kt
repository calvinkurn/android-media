package com.tokopedia.cart.domain.usecase

fun getSetCartlistCheckboxStateMutation(): String {
    return """
        mutation setCheckboxState(${'$'}params: [CartCheckboxStateParam]) {
          set_cartlist_checkbox_state(params: ${'$'}params) {
            status
            error_message
            data {
              success
            }
          }
        }
    """
}