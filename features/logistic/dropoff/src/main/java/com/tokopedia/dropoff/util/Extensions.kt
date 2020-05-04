package com.tokopedia.dropoff.util

internal fun com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffNearbyModel.getDescription(): String =
        "${this.districtName}, ${this.cityName}, ${this.provinceName}"