package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetDashboardProductStatisticsV2 : GqlQueryInterface {

    private const val OPERATION_NAME = "GetDashboardProductStatisticsV2"
    private const val PARAM_QUERY_INPUT = "queryInput"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(${'$'}$PARAM_QUERY_INPUT: GetDashboardProductStatisticsInputTypeV2!) {
              $OPERATION_NAME(queryInput: ${'$'}$PARAM_QUERY_INPUT) {
                data {
                  ad_id
                  stat_avg_click
                  stat_total_spent
                  stat_total_impression
                  stat_total_click
                  stat_total_ctr
                  stat_total_conversion
                  stat_total_sold
                  stat_total_gross_profit
                  stat_total_roas
                  stat_total_top_slot_impression
                }
              }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
