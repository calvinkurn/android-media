package com.tokopedia.tokopoints.view.coupondetail

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.ApplyCouponBaseEntity
import com.tokopedia.tokopoints.view.model.CouponDetailOuter
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdateOuter
import com.tokopedia.tokopoints.view.model.PhoneVerificationResponse
import com.tokopedia.tokopoints.view.util.CommonConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@TokoPointScope
class CouponDetailRepository @Inject constructor(private val repository: GraphqlRepository, private val map: Map<String, String>) {

    private val cacheStrategy by lazy {
        GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
    }


    suspend fun redeemCoupon(promoCode: String) = withContext(Dispatchers.IO) {
        val variables = HashMap<String, Any>()
        variables[CommonConstant.GraphqlVariableKeys.PROMO_CODE] = promoCode

        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_APPLY_COUPON],
                ApplyCouponBaseEntity::class.java,
                variables, false)
        repository.getReseponse(listOf(request), cacheStrategy)

    }


    suspend fun getCouponDetail(uniqueCouponCode: String) = withContext(Dispatchers.IO) {
        val variables = HashMap<String, Any>()
        variables[CommonConstant.GraphqlVariableKeys.CODE] = uniqueCouponCode
        variables[CommonConstant.GraphqlVariableKeys.APIVERSION] = CommonConstant.APIVERSION

        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_COUPON_DETAIL],
                CouponDetailOuter::class.java,
                variables, false)
        repository.getReseponse(listOf(request), cacheStrategy)
    }

    suspend fun reFetchRealCode(uniqueCouponCode: String) = withContext(Dispatchers.IO) {
        val variables = HashMap<String, Any>()
        variables[CommonConstant.GraphqlVariableKeys.CODE] = uniqueCouponCode
        variables[CommonConstant.GraphqlVariableKeys.APIVERSION] = CommonConstant.APIVERSION

        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_REFETCH_REAL_CODE],
                CouponDetailOuter::class.java,
                variables, false)
        repository.getReseponse(listOf(request), cacheStrategy)
    }


    suspend fun swipeMyCoupon(partnerCode: String, pin: String) = withContext(Dispatchers.IO) {
        val variables = HashMap<String, Any>()
        variables[CommonConstant.GraphqlVariableKeys.CODE] = partnerCode
        variables[CommonConstant.GraphqlVariableKeys.PIN] = pin

        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_SWIPE_COUPON],
                CouponSwipeUpdateOuter::class.java,
                variables, false)
        repository.getReseponse(listOf(request), cacheStrategy)
    }

    suspend fun getUserPhoneVerificationInfo() = withContext(Dispatchers.IO) {

        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_USER_INFO],
                PhoneVerificationResponse::class.java,
                null, false)
        repository.getReseponse(listOf(request), cacheStrategy)
    }
}