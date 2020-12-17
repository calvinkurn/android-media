package com.tokopedia.manageaddress.data.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.manageaddress.data.query.ShopLocationQuery
import com.tokopedia.manageaddress.domain.response.shoplocation.GetShopLocationResponse
import com.tokopedia.manageaddress.domain.response.shoplocation.SetShopLocationStatusResponse
import com.tokopedia.manageaddress.util.getResponse
import javax.inject.Inject

class ShopLocationRepository @Inject constructor(private val gql: GraphqlRepository) {

    suspend fun getShopLocation(shopId: Int?): GetShopLocationResponse {
        val param = mapOf("shop_id" to shopId)
        val request = GraphqlRequest(ShopLocationQuery.getShopLocation,
                GetShopLocationResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun setShopLocationStatus(warehouseId: Int, status: Int): SetShopLocationStatusResponse {
        val param = mapOf(
                "inputShopLocSetStatus" to listOf(mapOf(
                        "warehouse_id" to warehouseId,
                        "status" to status)
                )
        )
        val request = GraphqlRequest(ShopLocationQuery.setShopLocationStatus,
                SetShopLocationStatusResponse::class.java, param)
        return gql.getResponse(request)
    }


}