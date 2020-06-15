package com.tokopedia.discovery2.repository.claimCoupon

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse
import javax.inject.Inject


open class ClaimCouponGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), IClaimCouponGqlRepository {

    override suspend fun redeemCoupon(mapOf: Map<String, Any>): RedeemCouponResponse {
        val redeemCouponResponse = getGQLData(getGQLString(R.raw.mutation_redeem_coupon_gql),
                RedeemCouponResponse::class.java, mapOf) as RedeemCouponResponse
        return redeemCouponResponse
    }
}


