package com.tokopedia.buyerorder.detail.domain.queries

import com.tokopedia.buyerorder.detail.domain.queries.ActionButtonQuery.QUERY
import com.tokopedia.buyerorder.detail.domain.queries.ActionButtonQuery.QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * created by @bayazidnasir on 19/8/2022
 */

@GqlQuery(QUERY_NAME, QUERY)
internal object ActionButtonQuery {
    const val QUERY_NAME = "QuerySetActionButton"
    const val QUERY = """
        query tapactions(${"$"}param: [TapActionsArgs]!) {
          orderDetailTapAction(input: ${"$"}param) {
            label
            weight
            control
            header
            buttonType
            value
            method
            color{
              textColor
              border
              background
            }
            key
            name
            body {
             appURL
             method
             body
            }
          }
       }
    """
}