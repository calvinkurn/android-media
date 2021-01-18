package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticCommon.data.query.ShippingEditorQuery
import com.tokopedia.logisticCommon.data.response.shippingeditor.GetShipperListResponse
import com.tokopedia.logisticCommon.data.response.shippingeditor.GetShipperTickerResponse
import com.tokopedia.logisticCommon.data.utils.getResponse
import javax.inject.Inject

class ShippingEditorRepository @Inject constructor(private val gql: GraphqlRepository) {

    suspend fun getShippingEditor(shopId: Int): GetShipperListResponse {
        val param = mapOf("input" to mapOf("shop_id" to shopId))
        val request = GraphqlRequest(ShippingEditorQuery.ongkirShippingEditor,
                GetShipperListResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun getShippingEditorShipperTicker(shopId: Int): GetShipperTickerResponse {
        val param = mapOf("input" to mapOf("shop_id" to shopId))
        val request = GraphqlRequest(ShippingEditorQuery.ongkirShippingEditorTicker,
                GetShipperTickerResponse::class.java, param)
        return gql.getResponse(request)
    }
}