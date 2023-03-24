package com.tokopedia.discovery2.repository.claimCoupon

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.claim_coupon.ClaimCouponRequest
import com.tokopedia.discovery2.data.claim_coupon.ClaimCouponResponse
import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse
import javax.inject.Inject


open class ClaimCouponGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), IClaimCouponGqlRepository {

    override suspend fun getClickCouponData(claimCouponRequest: ClaimCouponRequest): ClaimCouponResponse {
        return getGQLData(getGQLString(R.raw.claim_coupon_gql), ClaimCouponResponse::class.java, setParams(claimCouponRequest))
    }

    override suspend fun redeemCoupon(mapOf: Map<String, Any>): RedeemCouponResponse {
        val redeemCouponResponse = getGQLData(getGQLString(R.raw.mutation_redeem_coupon_gql),
                RedeemCouponResponse::class.java, mapOf) as RedeemCouponResponse
        return redeemCouponResponse
    }

    fun setParams(claimCouponRequest: ClaimCouponRequest): Map<String,Any>{
        val params: Map<String, Any>?
        params = mapOf(
            PARAM_CATEGORY_SLUG to claimCouponRequest.categorySlug,
            PARAM_CATALOG_SLUGS to claimCouponRequest.catalogSlugs
        )

        return params
    }

    companion object{
        private const val PARAM_CATEGORY_SLUG = "categorySlug"
        private const val PARAM_CATALOG_SLUGS = "catalogSlugs"
    }
}


