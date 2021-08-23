package com.tokopedia.home_account.domain.query

object GetBalanceAndPointQuery {

    private const val partnerCode = "\$partnerCode"

    val gopayOvoQuery: String = """
        query wallet_app_get_account_balance($partnerCode: String!){
          walletappGetAccountBalance(partnerCode:$partnerCode){
            id
            icon
            title
            subtitle
            subtitle_color
            applink
            is_active
          }
        }
    """.trimIndent()

    val tokopointsQuery: String = """
        {
          tokopointsAccountPage{
            id
            icon
            title
            subtitle
            subtitle_color
            applink
            weblink
            is_active
            is_hidden
          }
        }
    """.trimIndent()
}