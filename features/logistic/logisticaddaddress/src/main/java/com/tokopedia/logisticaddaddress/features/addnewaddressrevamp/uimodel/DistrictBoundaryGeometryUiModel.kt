package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel

import com.google.android.gms.maps.model.LatLng

/**
 * Created by fwidjaja on 2019-06-10.
 */
data class DistrictBoundaryGeometryUiModel(
    var listCoordinates: MutableList<LatLng> = mutableListOf()
)
