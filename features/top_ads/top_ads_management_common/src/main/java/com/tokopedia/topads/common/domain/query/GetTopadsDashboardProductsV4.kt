package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsDashboardProductsV4 : GqlQueryInterface{

    private const val OPERATION_NAME = "topadsDashboardGroupProductsV4"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME (${ '$' } queryInput : topadsDashboardGroupProductsInputTypeV4 !) {
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
                ad_id
                item_id
                ad_status
                ad_status_desc
                ad_price_bid
                ad_price_bid_fmt
                ad_price_daily
                ad_price_daily_fmt
                stat_total_gross_profit
                ad_price_daily_spent_fmt
                ad_price_daily_bar
                product_name
                product_image_uri
                group_id
                group_name
            }
        }
        }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return  OPERATION_NAME
    }
}
