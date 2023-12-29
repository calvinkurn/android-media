package com.tokopedia.tokopedianow.util

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData

object AddressMapperTestUtils {

    private const val COMMA = ","
    private const val HASH_TAG = "#"

    fun mapToWarehousesData(addressData: LocalCacheModel?): List<WarehouseData> {
        return addressData?.warehouses.orEmpty().map {
            WarehouseData(it.warehouse_id.toString(), it.service_type)
        }
    }

    fun mapToWarehouses(addressData: LocalCacheModel?): String {
        return addressData?.warehouses.orEmpty().joinToString(COMMA) {
            "${it.warehouse_id}$HASH_TAG${it.service_type}"
        }
    }
}
