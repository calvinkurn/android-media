package com.tokopedia.kyc_centralized.gotoKyc.utils

import com.tokopedia.graphql.data.model.GraphqlResponse

inline fun <reified T> createSuccessResponse(data: T): GraphqlResponse =
    GraphqlResponse(
        mapOf(T::class.java to data),
        mapOf(),
        false
    )
