package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase

object GetTicker {

    val QUERY = """
        query ticker(${'$'}${GetTickerUseCase.PARAM_PAGE}: String, ${'$'}${GetTickerUseCase.PARAM_LOCATION}: String){
            ticker {
                tickers (page:${'$'}${GetTickerUseCase.PARAM_PAGE}, location:${'$'}${GetTickerUseCase.PARAM_LOCATION}) {
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