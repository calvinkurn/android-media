package com.tokopedia.logisticorder.mapper

import com.tokopedia.logisticorder.domain.response.LastDriver
import com.tokopedia.logisticorder.uimodel.LastDriverModel

class LastDriverMapper {

    fun mapLastDriverData(lastDriver: LastDriver): LastDriverModel {
        return LastDriverModel().apply {
            phone = lastDriver.phone
            name = lastDriver.name
            phone = lastDriver.phone
            licenseNumber = lastDriver.licenseNumber
            isChanged = lastDriver.isChanged
        }
    }
}