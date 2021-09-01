package com.tokopedia.home_account.domain.query

object GetTokopointsBalanceAndPointQuery {

    val query: String = """
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