package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponGqlRepository
import javax.inject.Inject

class ClaimCouponClickUseCase @Inject constructor(val claimCouponRepository: IClaimCouponGqlRepository) {

    suspend fun redeemCoupon(mapOf: Map<String, Any>): RedeemCouponResponse {
        val redeemCouponResponse = claimCouponRepository.redeemCoupon(mapOf)
        return redeemCouponResponse
    }
}