package com.tokopedia.common_wallet.balance.domain.query

object PendingCashback {
    val query = """
        {
            query pendingCashback {
              goalPendingBalance {
                balance
                balance_text
                cash_balance
                cash_balance_text
                point_balance
                point_balance_text
                wallet_type
                phone_number
                errors {
                  title
                  message
                }
              }
            }
        }
    """.trimIndent()
}