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