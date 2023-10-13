package com.tokopedia.discovery2.repository.quickcoupon

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.quickcouponresponse.ApplyCouponResponse
import com.tokopedia.discovery2.data.quickcouponresponse.PhoneVerificationResponse
import com.tokopedia.discovery2.data.quickcouponresponse.QuickCouponDetailResponse
import javax.inject.Inject


open class QuickCouponGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), QuickCouponRepository {
    override suspend fun getQuickCouponDetailData(pageIdentifier: String): QuickCouponDetailResponse {
        return getGQLData(quickCouponGql,
            QuickCouponDetailResponse::class.java, mapOf("discovery_page" to pageIdentifier))
    }

    override suspend fun getMobileVerificationData(): PhoneVerificationResponse {
        return getGQLData(mobileVerificationGql,
            PhoneVerificationResponse::class.java, mapOf())
    }

    override suspend fun applyUserQuickCoupon(promoCode: String): ApplyCouponResponse {
        return getGQLData(applyQuickCouponGql,
            ApplyCouponResponse::class.java, mapOf("promoCode" to promoCode))
    }

    private val quickCouponGql: String = """query GetOneClickCoupon(${'$'}discovery_page: String) {
    GetOneClickCoupon(discovery_page : ${'$'}discovery_page) {
      coupon_applied
      is_applicable
	   	code_promo
	  	real_code
		  catalog_title
		  coupon_app_link
		  message_using_success
		  message_using_failed
    }
  }""".trimIndent()

    private val mobileVerificationGql: String = """query getProfile {
    profile {
      phone_verified
    }
  }""".trimIndent()

    private val applyQuickCouponGql: String = """mutation save_cache_auto_apply(${'$'}promoCode: String) {
    save_cache_auto_apply(promoCode: ${'$'}promoCode) {
      Success
    }
  }""".trimIndent()
}


