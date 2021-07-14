package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase

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