package com.tokopedia.emoney.domain.query

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(JakCardUpdateQuery.QUERY_NAME, JakCardUpdateQuery.QUERY_VALUE)
object JakCardUpdateQuery {
    const val QUERY_NAME = "UpdateBalanceEmoneyDKIJakcard"
    const val QUERY_VALUE = """
         mutation $QUERY_NAME(${"$"}body: RechargeEmoneyDkiJakcardRequest!) {
                rechargeUpdateBalanceEmoneyDkiJakcard(body: ${"$"}body) {
                    action
                    status
                    attributes {
                        card_number
                        cryptogram
                        last_balance
                        button_text
                        image_issuer
                        message
                        stan
                        ref_no
                    }
                }
         }     
    """
}
