package com.tokopedia.tokopedianow.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetTargetedTickerNow: GqlQueryInterface {

    override fun getOperationNameList(): List<String> {
        return listOf("GetTargetedTicker")
    }

    override fun getQuery(): String {
        return """
        query GetTargetedTicker(${'$'}input: GetTargetedTickerRequest!) {
          GetTargetedTicker(input: ${'$'}input) {
            List{
              ID
              Title
              Content
              Action{
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
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return "GetTargetedTicker"
    }
}

