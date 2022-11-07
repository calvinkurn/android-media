package com.tokopedia.deals.pdp.domain.query

import com.tokopedia.deals.pdp.domain.query.DealsPDPContentQuery.DEALS_PDP_CONTENT_OPERATION_NAME
import com.tokopedia.deals.pdp.domain.query.DealsPDPContentQuery.DEALS_PDP_CONTENT_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(DEALS_PDP_CONTENT_OPERATION_NAME, DEALS_PDP_CONTENT_QUERY)
object DealsPDPContentQuery {
    const val DEALS_PDP_CONTENT_OPERATION_NAME = "EventContentByIdQuery"
    const val DEALS_PDP_CONTENT_QUERY = """
        query $DEALS_PDP_CONTENT_OPERATION_NAME(${'$'}typeID:String!, ${'$'}typeValue:String!){
            event_content_by_id (QueryInput: {typeID: ${'$'}typeID, typeValue: ${'$'}typeValue}) {
               data {
                 section_data {
                 content {
                    value_text
                   }
                 }
                }
            }
        }
    """
}
