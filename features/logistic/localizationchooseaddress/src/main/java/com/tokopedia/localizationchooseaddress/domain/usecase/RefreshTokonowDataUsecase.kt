package com.tokopedia.localizationchooseaddress.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.RefreshTokonowDataResponse
import javax.inject.Inject

class RefreshTokonowDataUsecase @Inject constructor(private val useCase: GraphqlUseCase<RefreshTokonowDataResponse.Data>) {

    init {
        useCase.setTypeClass(RefreshTokonowDataResponse.Data::class.java)
    }

    @GqlQuery(RefreshTokonowQuery, TOKONOW_REFRESH_USER_LCA_DATA)
    suspend fun execute(localCacheModel: LocalCacheModel): RefreshTokonowDataResponse.Data {
        useCase.setGraphqlQuery(RefreshTokonowQuery())
        useCase.setRequestParams(generateParam(localCacheModel))
        return useCase.executeOnBackground()
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
        private const val PARAM_LAST_UPDATE = "tokonowLastUpdate"
        private const val PARAM_DISTRICT_ID = "districtID"
        private const val PARAM_LATITUDE = "latitude"
        private const val PARAM_LONGITUDE = "longitude"
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_WAREHOUSE_ID = "warehouseID"
        private const val PARAM_SERVICE_TYPE = "serviceType"
        private const val PARAM_WAREHOUSES = "warehouses"
        private const val RefreshTokonowQuery = "RefreshTokonowQuery"
        private const val TOKONOW_REFRESH_USER_LCA_DATA = """
    query TokonowRefreshUserLCAData(${'$'}tokonowLastUpdate:String!, ${'$'}districtID:String!, ${'$'}latitude:String, ${'$'}longitude:String, ${'$'}shopID:String!, ${'$'}warehouseID:String!, ${'$'}serviceType:String!, ${'$'}warehouses:[RefreshUserLCAWarehouse!]!){
      TokonowRefreshUserLCAData(
        tokonowLastUpdate: ${'$'}tokonowLastUpdate,
        districtID: ${'$'}districtID, 
        latitude: ${'$'}latitude, 
        longitude: ${'$'}longitude,
        shopID: ${'$'}shopID, 
        warehouseID: ${'$'}warehouseID, 
        serviceType: ${'$'}serviceType, 
        warehouses: ${'$'}warehouses
      ) {
        header {
          process_time
          reason
          error_code
        }
        data {
          tokonowLastUpdate
          warehouseID
          shopID
          serviceType
          warehouses {
            serviceType
            warehouseID
          }
        }
      }
    }
    """
    }
}