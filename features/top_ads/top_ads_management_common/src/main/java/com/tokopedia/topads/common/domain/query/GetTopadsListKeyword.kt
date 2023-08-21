package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsListKeyword : GqlQueryInterface {

    private const val OPERATION_NAME = "topAdsListKeyword"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME (${ '$' } source : String !, ${ '$' } filter : TopAdsKeywordFilterReq, ${ '$' } page : TopAdsKeywordPageReq) {
            $OPERATION_NAME(source: ${ '$' } source, filter: ${ '$' } filter, page: ${ '$' } page) {
            error {
                code
                title
                detail
            }
            data {
                keywords {
                    keyword_id
                    shop_id
                    group_id
                    tag
                    type
                    status
                    price_bid
                    create_by
                    create_time_utc
                    update_by
                    update_time_utc
                }
                pagination {
                    next_cursor
                }
            }
        }
        }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
