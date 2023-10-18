package com.tokopedia.discovery2.repository.claimCoupon

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.claim_coupon.ClaimCouponRequest
import com.tokopedia.discovery2.data.claim_coupon.ClaimCouponResponse
import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse
import javax.inject.Inject


open class ClaimCouponGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), IClaimCouponGqlRepository {

    override suspend fun getClickCouponData(claimCouponRequest: ClaimCouponRequest): ClaimCouponResponse {
        return getGQLData(claimCouponGql, ClaimCouponResponse::class.java, setParams(claimCouponRequest))
    }

    override suspend fun redeemCoupon(mapOf: Map<String, Any>): RedeemCouponResponse {
        val redeemCouponResponse = getGQLData(mutationRedeemCouponGql,
            RedeemCouponResponse::class.java, mapOf) as RedeemCouponResponse
        return redeemCouponResponse
    }

    fun setParams(claimCouponRequest: ClaimCouponRequest): Map<String, Any> {
        val params: Map<String, Any>?
        params = mapOf(
            PARAM_CATEGORY_SLUG to claimCouponRequest.categorySlug,
            PARAM_CATALOG_SLUGS to claimCouponRequest.catalogSlugs
        )

        return params
    }


    companion object {
        private const val PARAM_CATEGORY_SLUG = "categorySlug"
        private const val PARAM_CATALOG_SLUGS = "catalogSlugs"
        private val claimCouponGql: String = """query Discovery_claimCoupons(${'$'}categorySlug: String!, ${'$'}catalogSlugs: [String!]){
  tokopointsCatalogWithCouponList(categorySlug: ${'$'}categorySlug, catalogSlugs : ${'$'}catalogSlugs) {
    resultStatus {
      code
      status
      message
    }
    catalogWithCouponList {
      id
      promoID
      quota
      title
      subTitle
      isDisabled
      disableErrorMessage
      thumbnailUrl
      thumbnailUrlMobile
      imageUrl
      imageUrlMobile
      slug
      baseCode
      upperTextDesc
      isDisabledButton
      catalogType
      couponCode
      cta
      ctaDesktop
      minimumUsageLabel
      minimumUsage
      smallImageUrl
      smallImageUrlMobile
      buttonStr
      url
      appLink
    }
    countdownInfo {
      isShown
      type
      label
      countdownUnix
      countdownStr
      textColor
      backgroundColor
    }
  }
}
""".trimIndent()

        private val mutationRedeemCouponGql: String = """mutation hachikoRedeem(${'$'}catalogId: Int!, ${'$'}isGift: Int!, ${'$'}giftUserId: Int!, ${'$'}giftEmail: String, ${'$'}notes: String ) {
  hachikoRedeem(catalog_id: ${'$'}catalogId, is_gift: ${'$'}isGift, gift_user_id: ${'$'}giftUserId, gift_email: ${'$'}giftEmail, notes: ${'$'}notes) {
    coupons{
      id
      owner
      promo_id
      code
      title
      description
      cta
      cta_desktop
      appLink
    }
    reward_points
    redeemMessage
  }
}""".trimIndent()

    }
}


