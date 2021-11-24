package com.tokopedia.ovop2p.constants

const val GQL_GET_WALLET_DETAIL = """query {
    wallet {
        cash_balance
        raw_cash_balance
        errors {
          name
          message
        }
    }
}"""

const val GQL_OVOP2P_TRANSFER_REQUEST =
    """mutation goalP2PRequest(${'$'}amount: Int!, ${'$'}to_phone_number: String!, ${'$'}message: String!) {
 goalP2PRequest(amount: ${'$'}amount, to_phone_number: ${'$'}to_phone_number, message: ${'$'}message) {
    dest_acc_name
    errors {
        title
        message
    }
  }
}
"""

const val GQL_OVOP2P_TRANSACTION_CONFIRM =
    """mutation goalP2PConfirm(${'$'}amount: Int!, ${'$'}to_phone_number: String!, ${'$'}message: String!) {
    goalP2PConfirm(amount: ${'$'}amount, to_phone_number: ${'$'}to_phone_number, message: ${'$'}message) {
        status
        transfer_id
        transaction_id
        pin_url
        receiver_link
        errors {
            title
            message
        }
    }
}"""


const val GQL_OVO_THNAKYOU_PAGE = """query goalP2PThanks(${'$'}transfer_id: Int!){
      goalP2PThanks(transfer_id: ${'$'}transfer_id) {
          transfer_id
          transaction_id
          status
          amount
          source_of_fund
          transfer_date
          destination {
              name
              phone
          }
          source {
              name
              phone
          }
          message
          reference_number
          errors {
              title
              message
          }
      }
  }
"""



