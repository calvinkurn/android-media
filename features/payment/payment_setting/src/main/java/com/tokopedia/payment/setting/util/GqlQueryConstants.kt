package com.tokopedia.payment.setting.util

const val GQL_DELETE_CREDIT_CARD_LIST = "gql_delete_credit_card_list"
const val GQL_CHECK_UPDATE_WHITE_LIST = "gql_check_update_white_list"
const val GQL_GET_CREDIT_CARD_LIST = "gql_get_credit_card_list"

const val GQL_DELETE_CREDIT_CARD_LIST1 = """mutation removeCreditCard(${'$'}token_id : String!) {
  removeCreditCard(token_id: ${'$'}token_id){
      success
      message
  }
}"""