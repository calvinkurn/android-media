package com.tokopedia.payment.setting.util

const val GQL_CHECK_UPDATE_WHITE_LIST = """mutation checkWhiteListStatus(${'$'}updateStatus : Boolean!, ${'$'}authValue : Int!, ${'$'}token : String) {
   checkWhiteListStatus(updateStatus: ${'$'}updateStatus, authValue: ${'$'}authValue, token : ${'$'}token) {
      status_code
      message
      data {
          user_email
          user_id
          state
          Token
      }
   }
}"""

const val GQL_DELETE_CREDIT_CARD_LIST = """mutation removeCreditCard(${'$'}token_id : String!) {
  removeCreditCard(token_id: ${'$'}token_id){
      success
      message
  }
}"""


const val GQL_GET_CREDIT_CARD_LIST = """{
PaymentQuery{
    paymentSignature {
      merchantCode
      profileCode
      ipAddress
      date
      userId
      customerName
      customerEmail
      callbackUrl
      hash
      customerMsisdn
    }
    creditCard{
        error
        cards {
            token_id
            masked_number
            expiry_month
            expiry_year
            card_type
            name_on_card
            alias
            card_number
            is_debit_online
            card_type_name
            type
            ccvault_code
            bank
            is_expired
            card_cvv
            bank_image
            card_type_image
            image
            small_background_image
            background_image
            is_registered_fingerprint
        }
    }
   }
}"""