package com.tokopedia.emoney.domain.query

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(JakCardUpdateQuery.QUERY_NAME, JakCardUpdateQuery.QUERY_VALUE)
object JakCardUpdateQuery {
    const val QUERY_NAME = "UpdateBalanceEmoneyDKIJakcard"
    const val QUERY_VALUE = """
         mutation $QUERY_NAME(${"$"}body: RechargeEmoneyDkiJakcardRequest!) {
                rechargeUpdateBalanceEmoneyDkiJakcard(body: ${"$"}body) {
                    encKey
                    encPayload
                }
         }     
    """
}
