package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticCommon.data.query.ShopLocationQuery
import com.tokopedia.logisticCommon.data.response.shoplocation.*
import com.tokopedia.logisticCommon.data.utils.getResponse
import javax.inject.Inject

class ShopLocationRepository @Inject constructor(@ApplicationContext private val gql: GraphqlRepository) {

    suspend fun getShopLocation(shopId: Long?): GetShopLocationResponse {
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

    suspend fun saveEditShopLocation(shopId: Long, warehouseId: Int, warehouseName: String,
                                     districtId: Int, latLon: String, email: String,
                                     addressDetail: String, postalCode: String, phone: String): ShopLocationUpdateWarehouseResponse {
        val param = mapOf(
                "inputShopLocUpdateWarehouse" to mapOf(
                        "shop_id" to shopId,
                        "warehouse_id" to warehouseId,
                        "warehouse_name" to warehouseName,
                        "district" to districtId,
                        "latlon" to latLon,
                        "email" to email,
                        "address_detail" to addressDetail,
                        "postal" to postalCode,
                        "phone" to phone
                )
        )
        val request = GraphqlRequest(ShopLocationQuery.shopLocUpdateWarehouse,
                ShopLocationUpdateWarehouseResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun getShopLocationWhitelist(shopId: Long): ShopLocationWhitelistResponse {
        val param = mapOf("shop_id" to shopId)
        val request = GraphqlRequest(ShopLocationQuery.shopLocationWhitelist,
                ShopLocationWhitelistResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun shopCheckCouriersNewLoc(shopId: Long, districtId: Int): ShopLocCheckCouriersNewLocResponse {
        val param = mapOf("shop_id" to shopId, "district_id" to districtId)
        val request = GraphqlRequest(ShopLocationQuery.shopLocCheckCouriers,
                ShopLocCheckCouriersNewLocResponse::class.java, param)
        return gql.getResponse(request)
    }
}