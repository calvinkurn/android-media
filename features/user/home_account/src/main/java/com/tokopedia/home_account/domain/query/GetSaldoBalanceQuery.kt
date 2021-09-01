package com.tokopedia.home_account.domain.query

object GetSaldoBalanceQuery {

    val query: String = """
        query {
          balance {
            buyer_all
            buyer_all_fmt
            buyer_hold
            buyer_hold_fmt
            buyer_usable
            buyer_usable_fmt
            seller_all
            seller_all_fmt
            seller_hold
            seller_hold_fmt
            seller_usable
            seller_usable_fmt
            have_error
            is_whitelist
          }
        }
    """.trimIndent()
}