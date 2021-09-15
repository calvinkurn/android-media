package com.tokopedia.logisticorder.mapper

import com.tokopedia.logisticorder.domain.response.DriverTipData
import com.tokopedia.logisticorder.domain.response.GetDriverTipResponse
import com.tokopedia.logisticorder.domain.response.LastDriver
import com.tokopedia.logisticorder.uimodel.DriverTipDataModel
import com.tokopedia.logisticorder.uimodel.LastDriverModel
import com.tokopedia.logisticorder.uimodel.LogisticDriverModel
import javax.inject.Inject

class DriverTipMapper @Inject constructor(){

    fun mapDriverTipData(response: GetDriverTipResponse): LogisticDriverModel {
        val data = response.response
        return LogisticDriverModel().apply {
            messageError = data.messageError
            status = data.status
            statusTitle = data.statusTitle
            statusSubtitle = data.statusSubtitle
            driverTipData = mapDriverTip(data.driverTipData)
        }
    }

    private fun mapDriverTip(response: DriverTipData): DriverTipDataModel {
        return DriverTipDataModel().apply {
            lastDriver = mapLastDriverData(response.lastDriver)
            status = response.status
            presetAmount = response.presetAmount
            maxAmount = response.maxAmount
            minAmount = response.minAmount
            paymentLink = response.paymentLink
        }
    }

    private fun mapLastDriverData(lastDriver: LastDriver): LastDriverModel {
        return LastDriverModel().apply {
            phone = lastDriver.phone
            name = lastDriver.name
            phone = lastDriver.phone
            licenseNumber = lastDriver.licenseNumber
            isChanged = lastDriver.isChanged
        }
    }
}