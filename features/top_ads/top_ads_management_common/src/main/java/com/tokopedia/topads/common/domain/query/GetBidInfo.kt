package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetBidInfo : GqlQueryInterface{

    private const val OPERATION_NAME = "topadsBidInfoV2"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME (
            ${ '$' } dataSuggestions :[BidInfoDataSuggestionsV2]!,
            ${ '$' } shopId : String !,
            ${ '$' } requestType : String !,
            ${ '$' } source : String !
            ){
                $OPERATION_NAME(dataSuggestions: ${ '$' } dataSuggestions, shopId: ${ '$' } shopId, requestType: ${ '$' } requestType, source: ${ '$' } source) {
                data {
                    id
                    max_bid
                    max_bid_fmt
                    min_bid
                    min_bid_fmt
                    min_daily_budget
                    max_daily_budget
                    min_daily_budget_fmt
                    max_daily_budget_fmt
                    multiplier
                    suggestion_bid
                    suggestion_bid_fmt
                    estimation {
                        min_bid
                        min_bid_fmt
                        max_bid
                        max_bid_fmt
                    }
                }
                request_type
            }
            }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
