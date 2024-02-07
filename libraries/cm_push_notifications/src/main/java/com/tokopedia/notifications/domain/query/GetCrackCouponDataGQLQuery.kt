package com.tokopedia.notifications.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class GetCrackCouponDataGQLQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return GQL_QUERY_GAMI_ANIMATION_CRACK_COUPON
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
