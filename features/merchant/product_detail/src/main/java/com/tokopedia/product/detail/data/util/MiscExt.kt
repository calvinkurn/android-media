package com.tokopedia.product.detail.data.util

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.data.model.constant.TimeUnitTypeDef
import com.tokopedia.product.detail.common.data.model.product.PreOrder
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.variant_common.model.VariantMultiOriginWarehouse

inline fun <reified T> GraphqlResponse.getSuccessData(): T {
    val error = getError(T::class.java)
    if (error == null || error.isEmpty()) {
        return getData(T::class.java)
    } else {
        throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
    }
}

val WarehouseInfo.origin: String?
    get() {
        return if (districtId.isNotBlank() && (postalCode.isNotBlank() || geoLocation.isNotBlank())) {
            arrayOf(districtId, postalCode, geoLocation).joinToString("|")
        } else null
    }

val VariantMultiOriginWarehouse.WarehouseInfo.origin: String?
    get() {
        return if (districtId.isNotBlank() && (postalCode.isNotBlank() || geoLocation.isNotBlank())) {
            arrayOf(districtId, postalCode, geoLocation).joinToString("|")
        } else null
    }

val PreOrder.timeUnitValue: String
    get() = if (timeUnit.toLowerCase() == TimeUnitTypeDef.DAY.toLowerCase()) "Hari"
    else if ((timeUnit.toLowerCase() == TimeUnitTypeDef.WEEK.toLowerCase())) "Minggu"
    else if ((timeUnit.toLowerCase() == TimeUnitTypeDef.MONTH.toLowerCase())) "Bulan"
    else ""