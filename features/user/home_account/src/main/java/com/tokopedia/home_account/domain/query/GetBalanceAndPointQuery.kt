package com.tokopedia.home_account.domain.query

object GetBalanceAndPointQuery {

    private const val partnerCode = "\$partnerCode"

    val query: String = """
        query wallet_app_get_account_balance($partnerCode: String!){
            walletappGetAccountBalance(partnerCode:$partnerCode){
                id
                icon
                title
                subtitle
                subtitle_color
                applink
                weblink
                is_active
                is_show
            }
        }
    """.trimIndent()

    val tokopointsQuery: String = """
        query{
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

    val saldoQuery: String = """
        query{
            midasGetSaldoWidgetBalance{
                id
                icon
                title
                subtitle
                subtitle_color
                applink
                weblink
                is_active
            }
        }
    """.trimIndent()

    val coBrandCCQuery: String = """
        query {
            cc_cobrand_getstatesforaccounts() {
                id
                icon
                title
                subtitle
                subtitle_color
                applink
                weblink
                is_active
            }
        }
    """.trimIndent()
}