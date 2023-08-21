package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsKeywordSuggestionV3_1 : GqlQueryInterface {

    private const val OPERATION_NAME = "topAdsGetKeywordSuggestionV3_1"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """query $OPERATION_NAME (${ '$' } product_ids : String !, ${ '$' } group_id : Int, ${ '$' } shop_id : String !, ${ '$' } type : Int) {
            $OPERATION_NAME(product_ids : ${ '$' } product_ids, group_id : ${ '$' } group_id, shop_id  : ${ '$' } shop_id, type : ${ '$' } type) {
            data {
                min_bid
                product_id
                keyword_data {
                    keyword
                    bid_suggest
                    total_search
                    source
                    competition
                }
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
