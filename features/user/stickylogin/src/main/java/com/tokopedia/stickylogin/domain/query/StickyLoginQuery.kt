package com.tokopedia.stickylogin.domain.query

object StickyLoginQuery {
    private const val page = "\$page"
    val query = """
        query get_ticker($page : String!) {
          ticker {
            tickers(page:$page) {
              message
              layout
            }
          }
        }
    """.trimIndent()
}