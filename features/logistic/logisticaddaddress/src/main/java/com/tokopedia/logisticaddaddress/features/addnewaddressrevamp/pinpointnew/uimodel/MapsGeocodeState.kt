package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.uimodel

import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.Location

sealed class MapsGeocodeState {
    data class Success(val location: Location) : MapsGeocodeState()
    data class Fail(val errorMessage: String?) : MapsGeocodeState()
}
