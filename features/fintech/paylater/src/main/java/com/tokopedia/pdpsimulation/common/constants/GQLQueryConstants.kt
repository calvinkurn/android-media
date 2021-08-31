package com.tokopedia.pdpsimulation.common.constants

const val GQL_PAY_LATER_PRODUCT_DETAIL = """query {
	paylater_getActiveProduct {
    product {
      id
      name
      subheader
      gateway_code
      img_light_url
      img_dark_url
      benefit {
        content
        is_highlight
      }
      faq {
        question
        answer
      }
      faq_url
      how_to_use {
        notes
        steps
      }
      how_to_apply {
        notes
        steps
      }
      is_able_apply
      how_to_action_url
    }
  }
}"""

const val GQL_PAY_LATER_APPLICATION_STATUS = """query getUserApplicationStatus{
  creditapplication_getUserApplicationStatus{
      tkp_user_id
     	application_detail{
        id
        gateway_name
        gateway_code
        application_status
        expiration_date
        app_status_content {
          email
          subheader
          phone_number
          pop_up_detail
          additional_info
        }
      }
    }
}"""

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


const val GQL_PAYLATER_SIMULATION_V2 = """query PaylaterGetSimulationV2(${'$'}Amount: Float) {
  paylater_getSimulationV2(Amount: ${'$'}Amount) {
    data {
      tenure
      text
      detail {
        gateway_id
        installment_per_month
        installment_per_month_ceil
        total_fee
        total_fee_ceil
        total_interest
        total_interest_ceil
        interest_pct
        total_with_provision
        total_with_provision_ceil
        is_recommended
        tenure
        activation_status
        account_status
        is_disabled
        cta {
          name
          description_1
          description_2
          web_url
          ios_url
          android_url
          cta_type
          is_redirect_url
          button_color
        }
        gateway_detail {
          gateway_id
          name
          product_code
          is_active
          subheader
          img_light_url
          img_dark_url
          faq_url
          apply_url
          benefit {
            content
            is_highlight
          }
          detail {
            title
            content
          }
          faq {
            question
            answer
          }
          how_to_use {
            notes
            steps
          }
          how_to_apply {
            notes
            steps
          }
          tnc
        }
      }
    } 
	}
}"""