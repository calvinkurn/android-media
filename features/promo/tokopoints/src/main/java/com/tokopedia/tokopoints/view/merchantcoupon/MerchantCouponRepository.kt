package com.tokopedia.tokopoints.view.merchantcoupon

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.view.model.merchantcoupon.MerchantCouponResponse
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class MerchantCouponRepository @Inject constructor(@Named(CommonConstant.GQLQuery.TP_GQL_REWARD_MERCHANTCOUPON)
                                                   private val tp_gql_merchantcoupon: String) {

    @Inject
    lateinit var mGetMerchantCouponUsecase: MultiRequestGraphqlUseCase

    suspend fun getProductData(page: Int, categoryId: String) = withContext(Dispatchers.IO) {
        mGetMerchantCouponUsecase.clearRequest()
        val variablesMain = java.util.HashMap<String, Any>()
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGE] = page
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGESIZE] = 10
        variablesMain[CommonConstant.GraphqlVariableKeys.CATEGORY_ID] = categoryId
        val request = GraphqlRequest(tp_gql_merchantcoupon,
                MerchantCouponResponse::class.java,variablesMain,false)
        mGetMerchantCouponUsecase.addRequest(request)
        mGetMerchantCouponUsecase.executeOnBackground()

    }
}