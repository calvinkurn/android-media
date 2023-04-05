package com.tokopedia.home.util

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel

object QueryParamUtils {
    private const val LOCATION_PARAM_FMT: String =
        "user_lat=%s&user_long=%s&user_addressId=%s&user_cityId=%s&user_districtId=%s&user_postCode=%s&warehouse_ids=%s"
    private const val WAREHOUSES_PARAM_FMT: String = "%s#%s"
    private const val WAREHOUSE_SEPARATOR = ","

    fun LocalCacheModel.convertToLocationParams(): String {
        return String.format(
            LOCATION_PARAM_FMT,
            lat,
            long,
            address_id,
            city_id,
            district_id,
            postal_code,
            warehouses.convertToWarehousesParam()
        )
    }

    fun List<LocalWarehouseModel>.convertToWarehousesParam(): String = filter { it.warehouse_id != 0L }.joinToString(WAREHOUSE_SEPARATOR) {
        WAREHOUSES_PARAM_FMT.format(it.warehouse_id, it.service_type)
    }
}
