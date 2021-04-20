package com.tokopedia.tokopoints.view.validatePin

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdateOuter
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.CommonConstant.GQLQuery.Companion.TP_GQL_SWIPE_COUPON
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SwipeCouponUseCase @Inject constructor(private val map : Map<String, String>, private val useCase: MultiRequestGraphqlUseCase) {
    suspend fun execute(code: String, pin: String) = withContext(Dispatchers.IO){
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CODE] = code
        variables[CommonConstant.GraphqlVariableKeys.PIN] = pin
        val graphqlRequestPoints = GraphqlRequest(map[TP_GQL_SWIPE_COUPON],
                CouponSwipeUpdateOuter::class.java, variables,false)
        useCase.addRequest(graphqlRequestPoints)
        useCase.executeOnBackground().getSuccessData<CouponSwipeUpdateOuter>()
    }
}