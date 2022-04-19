package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import javax.inject.Inject

class GeolocationMapper @Inject constructor() {
    fun map(input: KeroMapsAutofill): LocationPass {
        return LocationPass(
                input.data.latitude,
                input.data.longitude,
                input.data.title,
                input.data.formattedAddress,
                input.data.cityId.toString(),
                input.data.districtId.toString()
        )
    }
}