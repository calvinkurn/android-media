package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticCommon.data.query.ShopLocationQuery
import com.tokopedia.logisticCommon.data.request.shoplocation.KeroGetRolloutEligibilityParam
import com.tokopedia.logisticCommon.data.response.shoplocation.GetShopLocationResponse
import com.tokopedia.logisticCommon.data.response.shoplocation.KeroGetRolloutEligibilityResponse
import com.tokopedia.logisticCommon.data.response.shoplocation.SetShopLocationStatusResponse
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocCheckCouriersNewLocResponse
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocationUpdateWarehouseResponse
import com.tokopedia.logisticCommon.data.utils.getResponse
import javax.inject.Inject

class ShopLocationRepository @Inject constructor(@ApplicationContext private val gql: GraphqlRepository) {

    suspend fun getShopLocation(shopId: Long?): GetShopLocationResponse {
        val param = mapOf("shop_id" to shopId)
        val request = GraphqlRequest(
            ShopLocationQuery.getShopLocation,
            GetShopLocationResponse::class.java,
            param
        )
        return gql.getResponse(request)
    }

    suspend fun setShopLocationStatus(warehouseId: Long, status: Int): SetShopLocationStatusResponse {
        val param = mapOf(
            "inputShopLocSetStatus" to listOf(
                mapOf(
                    "warehouse_id" to warehouseId,
                    "status" to status
                )
            )
        )
        val request = GraphqlRequest(
            ShopLocationQuery.setShopLocationStatus,
            SetShopLocationStatusResponse::class.java,
            param
        )
        return gql.getResponse(request)
    }

    suspend fun saveEditShopLocation(
        shopId: Long,
        warehouseId: Long,
        warehouseName: String,
        districtId: Long,
        latLon: String,
        addressDetail: String,
        postalCode: String
    ): ShopLocationUpdateWarehouseResponse {
        val param = mapOf(
            "inputShopLocUpdateWarehouse" to mapOf(
                "shop_id" to shopId,
                "warehouse_id" to warehouseId,
                "warehouse_name" to warehouseName,
                "district" to districtId,
                "latlon" to latLon,
                "address_detail" to addressDetail,
                "postal" to postalCode
            )
        )
        val request = GraphqlRequest(
            ShopLocationQuery.shopLocUpdateWarehouse,
            ShopLocationUpdateWarehouseResponse::class.java,
            param
        )
        return gql.getResponse(request)
    }

    suspend fun getShopLocationWhitelist(shopId: Long): KeroGetRolloutEligibilityResponse {
        val param = KeroGetRolloutEligibilityParam(shopId = shopId).toMapParam()
        val request = GraphqlRequest(
            ShopLocationQuery.keroGetRolloutEligibility,
            KeroGetRolloutEligibilityResponse::class.java,
            param
        )
        return gql.getResponse(request)
    }

    suspend fun shopCheckCouriersNewLoc(shopId: Long, districtId: Long): ShopLocCheckCouriersNewLocResponse {
        val param = mapOf("shop_id" to shopId, "district_id" to districtId)
        val request = GraphqlRequest(
            ShopLocationQuery.shopLocCheckCouriers,
            ShopLocCheckCouriersNewLocResponse::class.java,
            param
        )
        return gql.getResponse(request)
    }
}
