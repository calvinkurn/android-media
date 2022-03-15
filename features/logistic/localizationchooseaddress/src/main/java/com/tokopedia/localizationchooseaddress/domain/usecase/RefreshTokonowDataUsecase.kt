package com.tokopedia.localizationchooseaddress.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.data.query.RefreshTokonowDataQuery
import com.tokopedia.localizationchooseaddress.domain.response.RefreshTokonowDataResponse
import kotlinx.coroutines.delay
import javax.inject.Inject

class RefreshTokonowDataUsecase @Inject constructor(private val useCase: GraphqlUseCase<RefreshTokonowDataResponse.Data>) {

    init {
        useCase.setTypeClass(RefreshTokonowDataResponse.Data::class.java)
    }

    suspend fun execute(localCacheModel: LocalCacheModel): RefreshTokonowDataResponse.Data {
//        useCase.setGraphqlQuery(RefreshTokonowDataQuery)
//        useCase.setRequestParams(generateParam(localCacheModel))
//        return useCase.executeOnBackground()
        delay(1000)
        return RefreshTokonowDataResponse.Data(
            RefreshTokonowDataResponse.Data.RefreshTokonowData(
                RefreshTokonowDataResponse.Data.RefreshTokonowData.RefreshTokonowDataSuccess(
                    shopId = "2",
                    serviceType = "15m",
                    lastUpdate = "2021-07-16T05:51:54+07:00",
                    warehouseId = "250508"
                )
            )
        )
    }

    private fun generateParam(localCacheModel: LocalCacheModel): Map<String, Any?> {
        return mapOf(
            PARAM_LAST_UPDATE to localCacheModel.tokonow_last_update,
            PARAM_DISTRICT_ID to localCacheModel.district_id,
            PARAM_LATITUDE to localCacheModel.lat,
            PARAM_LONGITUDE to localCacheModel.long,
            PARAM_SHOP_ID to localCacheModel.shop_id,
            PARAM_WAREHOUSE_ID to localCacheModel.warehouse_id,
            PARAM_SERVICE_TYPE to localCacheModel.service_type,
            PARAM_WAREHOUSES to localCacheModel.warehouses.map {
                mapOf(
                    PARAM_WAREHOUSE_ID to it.warehouse_id.toString(),
                    PARAM_SERVICE_TYPE to it.service_type
                )
            }
        )
    }

    companion object {
        private const val PARAM_LAST_UPDATE = "lastUpdate"
        private const val PARAM_DISTRICT_ID = "districtId"
        private const val PARAM_LATITUDE = "latitude"
        private const val PARAM_LONGITUDE = "longitude"
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_WAREHOUSE_ID = "warehouseId"
        private const val PARAM_SERVICE_TYPE = "serviceType"
        private const val PARAM_WAREHOUSES = "warehouses"
    }
}