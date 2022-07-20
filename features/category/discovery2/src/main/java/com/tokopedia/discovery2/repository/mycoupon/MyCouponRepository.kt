package com.tokopedia.discovery2.repository.mycoupon

import com.tokopedia.discovery2.data.mycoupon.MyCouponResponse
import com.tokopedia.discovery2.data.mycoupon.MyCouponsRequest

interface MyCouponRepository {
    suspend fun getCouponData(myCouponsRequest: MyCouponsRequest): MyCouponResponse
}