package com.tokopedia.pdp.fintech.constants

const val GQL_GET_WIDGET_DETAIL_V2 =
    """query PaylaterGetPDPWidgetV2(${'$'}request: PaylaterGetPDPWidgetV2Request!) {
  paylater_getPDPWidgetV2(request: ${'$'}request) {
    data {
      list {
        price
        title
        chips {
          gateway_id
          name
          product_code
          is_active
          is_disabled
          tenure
          header
          subheader
          subheader_color
          product_icon_light
          product_icon_dark
          user_state
          user_balance_amt
          installment_amt
          cta {
            type
            web_url
            android_url
            ios_url
            bottomsheet {
              show
              product_icon_light
              product_icon_dark
              title
              buttons {
                button_text
                button_text_color
                button_color
                button_url
              }
              descriptions {
                line_icon_dark
                line_icon_light
                text
              }
              product_footnote
              product_footnote_icon_light
              product_footnote_icon_dark
              footnote
              footnote_icon_light
              footnote_icon_dark
            }
          }
        }
      }
    }
  }
}"""