package com.tokopedia.cartrevamp.domain.usecase

const val SET_CHECKBOX_STATE_QUERY = """
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
