package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase

internal object GetTicker: GqlQueryInterface {

    private const val OPERATION_NAME = "ticker"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}${GetTickerUseCase.PARAM_PAGE}: String, ${'$'}${GetTickerUseCase.PARAM_LOCATION}: String){
            $OPERATION_NAME {
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

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}