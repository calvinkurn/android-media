package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 17/01/22.
 */

object GqlGetLayout : GqlQueryInterface {

    private const val TOP_OPERATION_NAME = "GetSellerDashboardLayout"
    private const val OPERATION_NAME = "GetSellerDashboardPageLayout"
    val QUERY = """
        query $TOP_OPERATION_NAME(${'$'}shopID: Int!, ${'$'}page: String!) {
          $OPERATION_NAME(shopID: ${'$'}shopID, page: ${'$'}page) {
            widget {
              ID
              widgetType
              title
              subtitle
              comparePeriode
              tooltip {
                title
                content
                show
                list {
                  title
                  description
                }
              }
              tag
              showEmpty
              postFilter {
                name
                value
              }
              url
              applink
              dataKey
              ctaText
              gridSize
              maxData
              maxDisplay
              emptyState {
                imageUrl
                title
                description
                ctaText
                applink
              }
              searchTableColumnFilter{
                name
                value
              }
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = TOP_OPERATION_NAME
}
