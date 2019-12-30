package com.tokopedia.tokopoints.view.coupondetail

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.NetworkDetector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Provider

@TokoPointScope
class CouponDetailRepository @Inject constructor(private val repository: GraphqlRepository,private val map : Map<String, String>) {

   private val cacheStrategy by lazy {
        GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
    }


//    suspend fun startValidateCoupon(item: CatalogsValueEntity) = withContext(Dispatchers.IO) {
//        val variables = HashMap<String, Any>()
//        variables[CommonConstant.GraphqlVariableKeys.CATALOG_ID] = item.id
//        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 0   //Never be a gift
//        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.tp_gql_tokopoint_validate_redeem),
//                ValidateCouponBaseEntity::class.java, variables, false)
//        repository.getReseponse(listOf(graphqlRequest), cacheStrategy)
//
//    }

    suspend fun redeemCoupon(promoCode: String) = withContext(Dispatchers.IO) {
        val variables = HashMap<String, Any>()
        variables[CommonConstant.GraphqlVariableKeys.PROMO_CODE] = promoCode

        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_APPLY_COUPON],
                ApplyCouponBaseEntity::class.java,
                variables, false)
        repository.getReseponse(listOf(request), cacheStrategy)

    }

//    suspend fun startSaveCoupon(item: CatalogsValueEntity) = withContext(Dispatchers.IO) {
//        val variables = HashMap<String, Any>()
//        variables[CommonConstant.GraphqlVariableKeys.CATALOG_ID] = item.id
//        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 0     //Never be a gift
//
//        val request = GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(),
//                R.raw.tp_gql_tokopoint_redeem_coupon),
//                RedeemCouponBaseEntity::class.java,
//                variables, false)
//        repository.getReseponse(listOf(request), cacheStrategy)
//    }

    suspend fun getCouponDetail(uniqueCouponCode: String) = withContext(Dispatchers.IO) {
        val variables = HashMap<String, Any>()
        variables[CommonConstant.GraphqlVariableKeys.CODE] = uniqueCouponCode

        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_COUPON_DETAIL],
                CouponDetailOuter::class.java,
                variables, false)
        repository.getReseponse(listOf(request), cacheStrategy)
    }

    suspend fun reFetchRealCode(uniqueCouponCode: String) = withContext(Dispatchers.IO) {
        val variables = HashMap<String, Any>()
        variables[CommonConstant.GraphqlVariableKeys.CODE] = uniqueCouponCode

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
}