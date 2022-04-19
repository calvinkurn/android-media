package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QuerySetInjectCouponTimeBased.SET_INJECT_COUPON_TIME_BASED_QUERY
import com.tokopedia.home.beranda.di.module.query.QuerySetInjectCouponTimeBased.SET_INJECT_COUPON_TIME_BASED_QUERY_NAME

@GqlQuery(SET_INJECT_COUPON_TIME_BASED_QUERY_NAME, SET_INJECT_COUPON_TIME_BASED_QUERY)
internal object QuerySetInjectCouponTimeBased {
    const val SET_INJECT_COUPON_TIME_BASED_QUERY_NAME = "SetInjectCouponTimeBasedQuery"
    const val SET_INJECT_COUPON_TIME_BASED_QUERY = "query setInjectCouponTimeBased{\n" +
            "            SetInjectCouponTimeBased {\n" +
            "                is_success\n" +
            "                error_message\n" +
            "            }\n" +
            "        }"
}