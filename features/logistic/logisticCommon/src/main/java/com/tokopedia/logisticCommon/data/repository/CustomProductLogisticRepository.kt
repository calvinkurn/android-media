package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticCommon.data.query.CustomProductLogisticQuery
import com.tokopedia.logisticCommon.data.response.customproductlogistic.OngkirGetCPLQGLResponse
import com.tokopedia.logisticCommon.data.utils.getResponse
import javax.inject.Inject

class CustomProductLogisticRepository @Inject constructor(@ApplicationContext private val gql: GraphqlRepository) {

    suspend fun getCPLList(shopId: Long, productId: String): OngkirGetCPLQGLResponse {
        val param = mapOf(
            "input" to mapOf(
                "shop_id" to shopId,"product_ids" to productId
            )
        )
        val request = GraphqlRequest(
            CustomProductLogisticQuery.getCPL,
            OngkirGetCPLQGLResponse::class.java, param
        )
        return gql.getResponse(request)
    }
}