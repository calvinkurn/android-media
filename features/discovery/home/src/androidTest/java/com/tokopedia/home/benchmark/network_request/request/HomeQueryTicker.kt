package com.tokopedia.home.benchmark.network_request.request

object HomeQueryTicker{
    fun getQuery() = """
        {
          ticker {
            meta {
              total_data
            }
            tickers
            {
              id
              title
              message
              color
              layout
            }
          }
        }
    """.trimIndent()
}