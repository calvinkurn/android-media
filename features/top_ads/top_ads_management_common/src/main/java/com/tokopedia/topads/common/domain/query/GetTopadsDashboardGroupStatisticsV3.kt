package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsDashboardGroupStatisticsV3 : GqlQueryInterface{

    private const val OPERATION_NAME = "GetTopadsDashboardGroupStatisticsV3"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """query $OPERATION_NAME (${ '$' } queryInput : GetTopadsDashboardGroupStatisticsInputTypeV3 !){
            $OPERATION_NAME(queryInput:${ '$' } queryInput){
            page {
                current
                per_page
                min
                max
                total
            }
            data {
                group_id
                stat_total_impression
                stat_total_click
                stat_total_conversion
            }
            errors {
                code
                detail
                title
            }
        }
        }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
