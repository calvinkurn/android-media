package com.tokopedia.tokofood.common.domain.param

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import java.lang.StringBuilder

object TokoFoodLocationParamMapper {

    private const val PARAM_USER_LAT = "user_lat"
    private const val PARAM_USER_LONG = "user_long"
    private const val PARAM_USER_CITY_ID = "user_cityId"
    private const val PARAM_USER_DISTRICT_ID = "user_districtId"
    private const val PARAM_USER_POSTAL_CODE = "user_postCode"
    private const val PARAM_USER_ADDRESS_ID = "user_addressId"
    private const val PARAM_WAREHOUSE_IDS = "warehouse_ids"

    fun mapLocation(localCacheModel: LocalCacheModel?): String {
        val stringBuilder = StringBuilder()
        localCacheModel?.run {
            val locationParamsMap = mutableMapOf<String, String>()

            locationParamsMap[PARAM_USER_LAT] = lat
            locationParamsMap[PARAM_USER_LONG] = long
            locationParamsMap[PARAM_USER_CITY_ID] = city_id
            locationParamsMap[PARAM_USER_DISTRICT_ID] = district_id
            locationParamsMap[PARAM_USER_POSTAL_CODE] = postal_code
            locationParamsMap[PARAM_USER_ADDRESS_ID] = address_id
            locationParamsMap[PARAM_WAREHOUSE_IDS] = warehouse_id

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