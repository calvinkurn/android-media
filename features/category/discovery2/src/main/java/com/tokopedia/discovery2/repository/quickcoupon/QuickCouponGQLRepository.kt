package com.tokopedia.discovery2.repository.quickcoupon

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.quickcouponresponse.ApplyCouponResponse
import com.tokopedia.discovery2.data.quickcouponresponse.PhoneVerificationResponse
import com.tokopedia.discovery2.data.quickcouponresponse.QuickCouponDetailResponse
import javax.inject.Inject


open class QuickCouponGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), QuickCouponRepository {
    override suspend fun getQuickCouponDetailData(pageIdentifier: String): QuickCouponDetailResponse {
        return getGQLData(getGQLString(R.raw.quick_coupon_gql),
                QuickCouponDetailResponse::class.java, mapOf("discovery_page" to pageIdentifier))
    }

    override suspend fun getMobileVerificationData(): PhoneVerificationResponse {
        return getGQLData(getGQLString(R.raw.mobile_verification_gql),
                PhoneVerificationResponse::class.java, mapOf())
    }

    override suspend fun applyUserQuickCoupon(promoCode: String): ApplyCouponResponse {
        return getGQLData(getGQLString(R.raw.apply_quick_coupon_gql),
                ApplyCouponResponse::class.java, mapOf("promoCode" to promoCode))
    }

}


