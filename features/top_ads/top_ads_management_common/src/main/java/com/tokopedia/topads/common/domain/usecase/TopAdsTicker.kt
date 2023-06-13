package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object TopAdsTicker: GqlQueryInterface {

    const val PARAM_SHOP_ID = "shopID"
    const val PARAM_TARGET_PAGE = "targetPage"
    const val PARAM_SOURCE = "source"
    const val TICKER_SOURCE_DASHBOARD = "dashboard"
    const val ANDROID = "android"
    private const val OPERATION_NAME = "DashboardTicker"

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}shopID: String!,${'$'}targetPage: String!) {
                topAdsTickerV2(shop_id: ${'$'}shopID, source: "android", target_page: ${'$'}targetPage) {
            data {
              message
              ticker_info {
                ticker_message: Message
                ticker_type: Type
              }
            }
            status {
              error_code
              message
            }
          }
        }
       """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }


    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }
}
