package com.tokopedia.tokopedianow.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference.SetUserPreferenceData
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.domain.query.SetUserPreferenceQuery
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SetUserPreferenceUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_WAREHOUSE_ID = "warehouseID"
        private const val PARAM_SERVICE_TYPE = "serviceType"
        private const val PARAM_WAREHOUSES = "warehouses"
    }

    private val graphql by lazy { GraphqlUseCase<SetUserPreference>(graphqlRepository) }

    suspend fun execute(localCacheModel: LocalCacheModel, serviceType: String): SetUserPreferenceData {
        graphql.apply {
            val warehouses = localCacheModel.warehouses.map {
                WarehouseData(
                    it.warehouse_id.toString(),
                    it.service_type
                )
            }

            val shopId = localCacheModel.shop_id
            val warehouse = warehouses.first { it.serviceType == serviceType }
            val warehouseId = warehouse.warehouseId

            val requestParams = RequestParams().apply {
                putString(PARAM_SHOP_ID, shopId)
                putString(PARAM_WAREHOUSE_ID, warehouseId)
                putString(PARAM_SERVICE_TYPE, serviceType)
                putObject(PARAM_WAREHOUSES, warehouses)
            }.parameters

            setGraphqlQuery(SetUserPreferenceQuery)
            setTypeClass(SetUserPreference::class.java)
            setRequestParams(requestParams)
        }

        return graphql.executeOnBackground().response.data
    }
}