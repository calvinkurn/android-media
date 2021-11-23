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