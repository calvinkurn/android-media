package com.tokopedia.pdpsimulation.common.constants

const val GQL_PAYLATER_SIMULATION_V3 = """
    query PaylaterGetSimulationV3(${'$'}request: PaylaterGetSimulationV3Request!) {
    paylater_getSimulationV3(request: ${'$'}request) {
      data {
        tenure
        text
        small_text
        label {
          text
          text_color
          bg_color
        }
        sections {
          title
          is_collapsible
          detail {
            gateway_detail {
              id
              name
              code
              img_light_url
              img_dark_url
              how_to_use {
                notes
                steps
              }
              payment_gateway_code
            }
            installment_per_month
            installment_per_month_ceil
            tenure
            subheader
            recommended {
              flag
              text
              color
            }
            benefits
            user_state
            user_balance_amt
            linking_status
            tenure_header
            disable {
              status
              header
            }
            cta {
              name
              android_url
              web_url
              cta_type
              button_color
              bottom_sheet {
                show
                image
                title
                description
                button_text
              }
              icon_url
            }
            installment_details {
              header
              content {
                title
                value
                type
              }
            }
          }
        }
      } 
    }
}
"""


const val GQL_GET_PRODUCT_DETAIL =
    """query GetProductV3(${'$'}productID: String!, ${'$'}options:OptionV3!){
  getProductV3(productID: ${'$'}productID, options: ${'$'}options) {
    productName
    url
    stock
    shop{
    id
    }
    price
     campaign {
      originalPrice
      discountedPrice
    }
    variant{
   selections{
    options{
      value
    }
  }
    products{
      productID
      combination
    }
  }
     pictures{
      urlThumbnail
    }
  }
}"""

const val GQL_PAYLATER_ACTIVATION = """
    query optimized_checkout(${'$'}request: PaylaterOptimizedCheckOutRequest!) {
    paylater_getOptimizedCheckout(request: ${'$'}request) {
      data {
      gateway_id
      payment_gateway_code
      gateway_name
      subtitle
      subtitle2
      light_img_url
      dark_img_url
      disable
      label
      reason_short
      reason_long
      user_state
      user_balance_amt
      detail {
        tenure
        tenure_disable
        label
        chip_title
        monthly_installment
        description
        installment_details {
          header
          content {
            title
            value
            type
          }
        }
      }
    }
    footer
    }
}
"""
