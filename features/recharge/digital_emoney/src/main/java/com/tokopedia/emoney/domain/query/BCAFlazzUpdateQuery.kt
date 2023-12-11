package com.tokopedia.emoney.domain.query

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(BCAFlazzUpdateQuery.QUERY_NAME, BCAFlazzUpdateQuery.QUERY_VALUE)
object BCAFlazzUpdateQuery {
    const val QUERY_NAME = "UpdateBalanceBCAFlazz"
    const val QUERY_VALUE = """
        mutation $QUERY_NAME(${"$"}body: RechargeEmoneyBcaFlazzRequest!) {
            rechargeUpdateBalanceEmoneyBcaFlazz(body: ${"$"}body) {
                    encKey
                    encPayload
                }
        }
    """
}
