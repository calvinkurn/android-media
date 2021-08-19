package com.tokopedia.home_account.domain.query

object GetBalanceAndPointQuery {

    private const val partnerCode = "\$partnerCode"

    val query: String = """
        query wallet_app_get_account_balance($partnerCode: String!){
            walletappGetAccountBalance(partnerCode:$partnerCode){
                id
                title
                subtitle
                subtitle_color
                applink
                icon
                is_active
            }
        }
    """.trimIndent()
}