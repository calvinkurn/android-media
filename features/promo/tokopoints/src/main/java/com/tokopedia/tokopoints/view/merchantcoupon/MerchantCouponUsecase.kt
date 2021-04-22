package com.tokopedia.tokopoints.view.merchantcoupon

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.view.model.merchantcoupon.MerchantCouponResponse
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.PAGE_SIZE
import com.tokopedia.tokopoints.view.util.CommonConstant.GraphqlVariableKeys.Companion.CATEGORY_ROOTID
import com.tokopedia.tokopoints.view.util.CommonConstant.GraphqlVariableKeys.Companion.PAGE
import com.tokopedia.tokopoints.view.util.CommonConstant.GraphqlVariableKeys.Companion.PAGESIZE
import com.tokopedia.tokopoints.view.util.TP_CATALOG_MVC_LIST_QUERY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@GqlQuery("TpMvcCatalogList", TP_CATALOG_MVC_LIST_QUERY)
class MerchantCouponUsecase @Inject constructor(val graphqlRepository: GraphqlRepository)  {

    companion object {
        @JvmStatic
        fun createParams(page: Int, categoryId: String): Map<String, Any> {
            return mapOf(PAGE to page, PAGESIZE to PAGE_SIZE, CATEGORY_ROOTID to categoryId)
        }
    }

    var params = mapOf<String, Any>()

     suspend fun executeOnBackground() = withContext(Dispatchers.IO)  {
        val gqlRequest = GraphqlRequest(TpMvcCatalogList.GQL_QUERY, MerchantCouponResponse::class.java, params ,false)
        graphqlRepository.getReseponse(listOf(gqlRequest))
    }
}