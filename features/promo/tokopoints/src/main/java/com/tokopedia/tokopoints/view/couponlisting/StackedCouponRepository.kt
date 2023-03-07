package com.tokopedia.tokopoints.view.couponlisting

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.view.model.CouponFilterBase
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity
import com.tokopedia.tokopoints.view.util.CommonConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StackedCouponRepository @Inject constructor(private val repository: GraphqlRepository, private val map: Map<String, String>) {

    internal suspend fun getFilter(slug: String) = withContext(Dispatchers.IO) {
        val variablesFilter = HashMap<String, Any>()
        variablesFilter.put(CommonConstant.GraphqlVariableKeys.SLUG, slug.toLowerCase())
        val filterRequest = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_COUPON_FILTER],
                CouponFilterBase::class.java, variablesFilter, false)
        repository.response(listOf(filterRequest))
    }

    internal suspend fun getCouponList(pageNumber: Int, category: Int) = withContext(Dispatchers.IO) {
        val variablesMain = java.util.HashMap<String, Any>()
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGE] = pageNumber
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGE_SIZE] = CommonConstant.HOMEPAGE_PAGE_SIZE
        variablesMain[CommonConstant.GraphqlVariableKeys.SERVICE_ID] = ""
        variablesMain[CommonConstant.GraphqlVariableKeys.CATEGORY_ID_COUPON] = category
        variablesMain[CommonConstant.GraphqlVariableKeys.CATEGORY_ID] = 0
        variablesMain[CommonConstant.GraphqlVariableKeys.APIVERSION] = CommonConstant.APIVERSION

        val graphqlRequestMain = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_COUPON_LISTING_STACK], TokoPointPromosEntity::class.java, variablesMain, false)
        repository.response(listOf(graphqlRequestMain))
    }

    internal suspend fun getInStackedCouponList(stackId: String) = withContext(Dispatchers.IO) {
        //Adding request for main query
        val variablesMain = java.util.HashMap<String, Any>()
        variablesMain[CommonConstant.GraphqlVariableKeys.STACK_ID] = stackId
        val graphqlRequestMain = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_COUPON_IN_STACK], TokoPointPromosEntity::class.java, variablesMain, false)
        repository.response(listOf(graphqlRequestMain))
    }
}
