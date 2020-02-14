package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponRepository
import javax.inject.Inject

class ClaimCouponUseCase @Inject constructor(val claimCouponRepository: IClaimCouponRepository) {

    suspend fun getClickCouponData(endPoint: String): ArrayList<ComponentsItem> {
        return claimCouponRepository.getClickCouponData(endPoint)
    }
}