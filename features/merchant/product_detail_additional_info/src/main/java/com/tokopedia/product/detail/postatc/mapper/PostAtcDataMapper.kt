package com.tokopedia.product.detail.postatc.mapper

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest

internal fun LocalCacheModel.generateUserLocationRequest(): UserLocationRequest {
    val latlong = if (lat.isEmpty() && long.isEmpty()) "" else "${lat},${long}"
    return UserLocationRequest(
        districtID = district_id,
        addressID = address_id,
        postalCode = postal_code,
        latlon = latlong,
        cityId = city_id,
        addressName = label
    )
}


