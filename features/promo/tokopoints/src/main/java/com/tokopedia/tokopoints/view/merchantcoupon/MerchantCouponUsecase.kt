package com.tokopedia.tokopoints.view.merchantcoupon

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopoints.view.model.merchantcoupon.MerchantCouponResponse
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.PAGE_SIZE
import com.tokopedia.tokopoints.view.util.CommonConstant.GraphqlVariableKeys.Companion.CATEGORY_ROOTID
import com.tokopedia.tokopoints.view.util.CommonConstant.GraphqlVariableKeys.Companion.PAGE
import com.tokopedia.tokopoints.view.util.CommonConstant.GraphqlVariableKeys.Companion.PAGESIZE
import com.tokopedia.tokopoints.view.util.TP_CATALOG_MVC_LIST_QUERY
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

@GqlQuery("TpMvcCatalogList", TP_CATALOG_MVC_LIST_QUERY)
class MerchantCouponUsecase @Inject constructor(val graphqlRepository: GraphqlRepository) : UseCase<MerchantCouponResponse>() {

    companion object {
        @JvmStatic
        fun createParams(page: Int, categoryId: String): Map<String, Any> {
            return mapOf(PAGE to page, PAGESIZE to PAGE_SIZE, CATEGORY_ROOTID to categoryId)
        }
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): MerchantCouponResponse {
        val gqlRequest = GraphqlRequest(TpMvcCatalogList.GQL_QUERY, MerchantCouponResponse::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<MerchantCouponResponse>(MerchantCouponResponse::class.java)
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }
}