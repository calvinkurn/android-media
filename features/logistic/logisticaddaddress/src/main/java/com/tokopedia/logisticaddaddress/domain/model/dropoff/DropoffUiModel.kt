package com.tokopedia.logisticaddaddress.domain.model.dropoff

import com.tokopedia.logisticdata.data.entity.address.LocationDataModel

data class DropoffUiModel(val nearbyStores: List<LocationDataModel>, val radius: Int)