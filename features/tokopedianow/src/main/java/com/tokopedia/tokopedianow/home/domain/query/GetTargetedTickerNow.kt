package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.tokopedianow.home.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase

internal object GetTargetedTickerNow: GqlQueryInterface {

    private const val OPERATION_NAME = "GetTargetedTicker"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}${GetTargetedTickerUseCase.PARAM_PAGE}: String, ${'$'}${GetTargetedTickerUseCase.PARAM_TARGET}: [GetTargetedTickerRequestTarget]!){
            GetTargetedTicker(input:{Page: ${'$'}page, Target: ${'$'}target}) {
                List {
                  ID
                  Title
                  Content
                  Action {
                    Label
                    Type
                    AppURL
                    WebURL
                  }
                  Type
                  Priority
                  Metadata{
                    Type
                    Values
                  }
                }
              }
            }
        }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
