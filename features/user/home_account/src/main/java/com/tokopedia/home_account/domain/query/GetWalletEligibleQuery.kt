package com.tokopedia.home_account.domain.query

object GetWalletEligibleQuery {

    private const val partnerCode = "\$partnerCode"
    private const val walletCode = "\$walletCode"


    val query: String = """
        query get_wallet_eligible($partnerCode: String!, $walletCode: String!){
          walletappGetWalletEligible(partnerCode: $partnerCode, walletCode: [$walletCode]) {
            code 
            message
            data {
              wallet_code
              is_eligible
            }
          }
        }
    """.trimIndent()
}