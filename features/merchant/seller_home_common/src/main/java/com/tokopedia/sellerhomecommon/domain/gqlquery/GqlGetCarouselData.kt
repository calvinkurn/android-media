package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 17/01/22.
 */

object GqlGetCarouselData : GqlQueryInterface {

    private const val TOP_OPERATION_NAME = "getCarouselWidgetData"
    private const val OPERATION_NAME = "fetchCarouselWidgetData"
    val QUERY = """
        query $TOP_OPERATION_NAME(${'$'}dataKeys: [dataKey!]!) {
          $OPERATION_NAME(dataKeys: ${'$'}dataKeys) {
            data {
              dataKey
              items {
                ID
                URL
                CreativeName
                AppLink
                FeaturedMediaURL
              }
              errorMsg
              showWidget
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> {
        return listOf()
    }

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = TOP_OPERATION_NAME
}

