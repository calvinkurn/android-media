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
        query tokopoints_account_page {
            tokopointsAccountPage {
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
        query saldo_widget_balance {
            midasGetSaldoWidgetBalance {
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
        query cobrand_get_states_for_accounts {
            cc_cobrand_getstatesforaccounts {
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