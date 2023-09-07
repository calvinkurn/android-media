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
          linking_status
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
          promo_name
        }
      }
    }
  }
}"""

const val GQL_GET_WIDGET_DETAIL_V3 =
    """query PaylaterGetPDPWidgetV3(${'$'}request: PaylaterGetPDPWidgetV3Request!) {
  paylater_getPDPWidgetV3(request: ${'$'}request) {
    data {
      list {
        icon_url_light
        icon_url_dark
        web_url
        android_url
        ios_url
        messages
        price
        product_id
        usecase_rank
        installment_amt
        linking_status
        user_state
        widget_type
        product_code
      }
    }
  }
}"""

