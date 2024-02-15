package com.tokopedia.discovery2.usecase

import android.annotation.SuppressLint
import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponGqlRepository
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import javax.inject.Inject

class ClaimCouponClickUseCase @Inject constructor(val claimCouponRepository: IClaimCouponGqlRepository) {

    suspend fun redeemCoupon(catalogId: Long): RedeemCouponResponse {
        return claimCouponRepository.redeemCoupon(getQueryMap(catalogId))
    }

    @SuppressLint("PII Data Exposure")
    private fun getQueryMap(catalogId: Long): Map<String, Any> {
        return mapOf(
            CATALOG_ID to catalogId,
            IS_GIFT to Int.ZERO,
            GIFT_USER_ID to Int.ZERO,
            GIFT_EMAIL to String.EMPTY,
            NOTES to String.EMPTY
        )
    }

    companion object {
        private const val CATALOG_ID = "catalogId"
        private const val IS_GIFT = "isGift"
        private const val GIFT_USER_ID = "giftUserId"
        private const val GIFT_EMAIL = "giftEmail"
        private const val NOTES = "notes"
    }
}
