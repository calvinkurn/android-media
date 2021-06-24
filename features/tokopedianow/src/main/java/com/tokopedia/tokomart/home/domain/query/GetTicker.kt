package com.tokopedia.tokomart.home.domain.query

import com.tokopedia.tokomart.home.domain.usecase.GetTickerUseCase

object GetTicker {

    val QUERY = """
        query ticker(${'$'}${GetTickerUseCase.PAGE}: String){
            ticker {
                tickers (page:${'$'}${GetTickerUseCase.PAGE}) {
                  id
                  title
                  message
                  color
                }
            }
        }
    """.trimIndent()
}