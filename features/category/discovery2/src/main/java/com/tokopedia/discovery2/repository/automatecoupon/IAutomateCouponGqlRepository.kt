package com.tokopedia.discovery2.repository.automatecoupon

import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponRequest
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponResponse

interface IAutomateCouponGqlRepository {
    suspend fun fetchData(request: AutomateCouponRequest): AutomateCouponResponse
}
