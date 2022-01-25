package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.query.GetHomeLayoutData
import com.tokopedia.usecase.RequestParams
import java.lang.StringBuilder
import javax.inject.Inject

/**
 * Dynamic Home Channel Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/381550603/HPB+Home+-+Mojito+Channel
 */

class GetHomeLayoutDataUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetHomeLayoutResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_TOKEN = "token"
        private const val PARAM_NUM_OF_CHANNEL = "numOfChannel"
        private const val PARAM_LOCATION = "location"
        private const val PARAM_USER_LAT = "user_lat"
        private const val PARAM_USER_LONG = "user_long"
        private const val PARAM_USER_CITY_ID = "user_cityId"
        private const val PARAM_USER_DISTRICT_ID = "user_districtId"
        private const val PARAM_USER_POSTAL_CODE = "user_postCode"
        private const val PARAM_USER_ADDRESS_ID = "user_addressId"
        private const val PARAM_WAREHOUSE_IDS = "warehouse_ids"
        private const val PARAM_SERVICE_TYPE_2H = "servicetype_2h"
        private const val PARAM_SERVICE_TYPE_15M = "servicetype_15m"
        private const val PARAM_SERVICE_TYPE = "servicetype"

        private const val DEFAULT_TOKEN = ""
        private const val DEFAULT_NUM_OF_CHANNEL = 10
    }

    init {
        setGraphqlQuery(GetHomeLayoutData.QUERY)
        setTypeClass(GetHomeLayoutResponse::class.java)
    }

    suspend fun execute(
        token: String = DEFAULT_TOKEN,
        numOfChannel: Int = DEFAULT_NUM_OF_CHANNEL,
        localCacheModel: LocalCacheModel?
    ): List<HomeLayoutResponse> {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_TOKEN, token)
            putInt(PARAM_NUM_OF_CHANNEL, numOfChannel)
            putString(PARAM_LOCATION, mapLocation(localCacheModel))
        }.parameters)

        val response = executeOnBackground().response
        return response.data
    }

    private fun mapLocation(localCacheModel: LocalCacheModel?): String {
        val stringBuilder = StringBuilder()
        localCacheModel?.run {
            val locationParamsMap = mutableMapOf<String, String>()

            val warehouse15m = localCacheModel.warehouses.find {
                it.service_type == ServiceType.NOW_15M
            }
            val warehouse2h = localCacheModel.warehouses.find {
                it.service_type == ServiceType.NOW_2H
            }

            val warehouseId15m = warehouse15m?.warehouse_id.toString()
            val warehouseId2h = warehouse2h?.warehouse_id.toString()

            locationParamsMap[PARAM_USER_LAT] = lat
            locationParamsMap[PARAM_USER_LONG] = long
            locationParamsMap[PARAM_USER_CITY_ID] = city_id
            locationParamsMap[PARAM_USER_DISTRICT_ID] = district_id
            locationParamsMap[PARAM_USER_POSTAL_CODE] = postal_code
            locationParamsMap[PARAM_USER_ADDRESS_ID] = address_id
            locationParamsMap[PARAM_WAREHOUSE_IDS] = warehouse_id
            locationParamsMap[PARAM_SERVICE_TYPE] = service_type
            locationParamsMap[PARAM_SERVICE_TYPE_15M] = warehouseId15m
            locationParamsMap[PARAM_SERVICE_TYPE_2H] = warehouseId2h

            for((key, value) in locationParamsMap) {
                if(stringBuilder.isNotBlank()) {
                    stringBuilder.append("&")
                }
                stringBuilder.append("$key=$value")
            }
        }
        return stringBuilder.toString()
    }
}