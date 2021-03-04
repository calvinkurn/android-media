package com.tokopedia.dropoff.util

import com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffNearbyModel

internal fun DropoffNearbyModel.getDescription(): String =
        "${this.districtName}, ${this.cityName}, ${this.provinceName}"