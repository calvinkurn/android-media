package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.mycoupon.MyCouponResponse
import com.tokopedia.discovery2.data.mycoupon.MyCouponsRequest
import com.tokopedia.discovery2.repository.mycoupon.MyCouponRepository
import javax.inject.Inject

class MyCouponUseCase @Inject constructor(private val myCouponRepository: MyCouponRepository) {

    suspend fun getMyCouponData(myCouponsRequest: MyCouponsRequest): MyCouponResponse {
        return myCouponRepository.getCouponData(myCouponsRequest)
    }
}