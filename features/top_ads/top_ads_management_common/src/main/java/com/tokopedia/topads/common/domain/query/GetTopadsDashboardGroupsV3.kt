package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsDashboardGroupsV3 : GqlQueryInterface{

    private const val OPERATION_NAME = "GetTopadsDashboardGroupsV3"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME (${ '$' } queryInput : GetTopadsDashboardGroupsInputTypeV3 !) {
            $OPERATION_NAME(queryInput: ${ '$' } queryInput) {
            separate_statistic
            meta {
                page {
                    per_page
                    current
                    total
                }
            }
            data {
                group_id
                total_item
                total_keyword
                group_status
                group_status_desc
                group_status_toogle
                group_price_bid
                group_price_daily
                group_price_daily_spent_fmt
                group_price_daily_bar
                group_name
                group_type
                group_end_date
                stat_total_conversion
                stat_total_spent
                stat_total_ctr
                stat_total_sold
                stat_avg_click
                stat_total_income
                strategies
            }
        }
        }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
