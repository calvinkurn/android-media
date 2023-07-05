package com.tokopedia.recommendation_widget_common.ext

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel

/**
 * Created by Lukas on 2/24/21.
 */
private const val WAREHOUSES_PARAM_FMT = "%s#%s"
private const val WAREHOUSE_SEPARATOR = ","

fun LocalCacheModel.toQueryParam(queryParam: String): String {
    val addressId = "user_addressId=" + this.address_id
    val cityId = "user_cityId=" + this.city_id
    val districtId = "user_districtId=" + this.district_id
    val lat = "user_lat=" + this.lat
    val long = "user_long=" + this.long
    val postCode = "user_postCode=" + this.postal_code
    val formattedWarehouses = "warehouse_ids=" + this.warehouses.convertToWarehousesParam()
    return (if (queryParam.isEmpty()) "" else "$queryParam&") +
        "%s&%s&%s&%s&%s&%s&%s".format(addressId, cityId, districtId, lat, long, postCode, formattedWarehouses)
}

fun List<LocalWarehouseModel>.convertToWarehousesParam(): String = joinToString(WAREHOUSE_SEPARATOR) {
    WAREHOUSES_PARAM_FMT.format(it.warehouse_id, it.service_type)
}
