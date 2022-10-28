package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object AgreeConsentQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return listOf("tokofoodSubmitUserConsent")
    }

    override fun getTopOperationName(): String {
        return "AgreeConsent"
    }

    override fun getQuery(): String {
        return GQL_QUERY
    }

    const val GQL_QUERY =
        "mutation AgreeConsent { tokofoodSubmitUserConsent { message success } }"
}
