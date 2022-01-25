package com.tokopedia.pdpsimulation.common.constants


const val GQL_CREDIT_CARD_SIMULATION = """query cc_fetchpdpcreditcardsimulation(${'$'}Amount: Float){
  cc_fetchpdpcreditcardsimulation(Amount: ${'$'}Amount) {
   	data {
      cicilan {
        tenure_id
        tenure_desc
        min_amount
        installment_amount
        total_bank
        is_popular
        bank {
          bank_id
          bank_name
          transaction_benefit
          available_duration
          bank_image_url
        }
        is_disabled
      }
   	  principal_amount
   	  ticker_info
   	  cta_main_label
   	  cta_description
   	}
  }
}"""


const val GQL_CREDIT_CARD_PDP_META_INFO = """query {
  cc_fetchpdpinfometadata() {
    cta_redir_url
    cta_redir_applink
    cta_redir_label
    pdp_info_content {
      title
      description
      notes
      content
      content_parse
    }
  }
}"""

const val GQL_CREDIT_CARD_BANK_LIST = """query {
  cc_fetchbankcardlist() {
    data {
      bank_name
      bank_slug
      bank_logo_url
      bank_pdp_info
      card_list {
        card_name
        card_slug
        card_image_url
        special_label
        main_benefit
        is_special_offer
      }
    }
  }
}"""

const val GQL_PAYLATER_SIMULATION_V3 = """
    query PaylaterGetSimulationV3(${'$'}request: PaylaterGetSimulationV3Request!) {
    paylater_getSimulationV3(request: ${'$'}request) {
      data {
        tenure
        text
        small_text
        sections {
          title
          is_collapsible
          detail {
            gateway_detail {
              id
              name
              img_light_url
              img_dark_url
              how_to_use {
                notes
                steps
              }
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
            disable {
              status
              header
              description
            }
            cta {
              name
              android_url
              web_url
              cta_type
              is_redirect_url
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
    price
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