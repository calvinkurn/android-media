package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticCommon.data.query.ShippingEditorQuery
import com.tokopedia.logisticCommon.data.response.shippingeditor.*
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

    suspend fun getShipperDetails(): GetShipperDetailResponse {
        val request = GraphqlRequest(ShippingEditorQuery.getShipperDetails, GetShipperDetailResponse::class.java)
        return gql.getResponse(request)
    }

    suspend fun validateShippingEditor(shopId: Int, activatedSpId: String): ValidateShippingEditorResponse {
        val param = mapOf("input" to mapOf(
                "shop_id" to shopId,
                "activated_sp_id" to activatedSpId))
        val request = GraphqlRequest(ShippingEditorQuery.ongkirShippingEditorPopup,
                ValidateShippingEditorResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun saveShippingEditor(shopId: Int, activatedSpId: String, featureId: String): SaveShippingEditorResponse {
        val param  = mapOf("input" to mapOf(
                "shop_id" to shopId,
                "activated_sp_id" to activatedSpId,
                "feature_id" to featureId))
        val request = GraphqlRequest(ShippingEditorQuery.saveShippingEditor,
                SaveShippingEditorResponse::class.java, param)
        return gql.getResponse(request)
    }
}