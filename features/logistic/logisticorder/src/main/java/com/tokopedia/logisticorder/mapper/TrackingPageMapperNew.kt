package com.tokopedia.logisticorder.mapper

import com.tokopedia.logisticorder.domain.response.*
import com.tokopedia.logisticorder.uimodel.*
import javax.inject.Inject

class TrackingPageMapperNew @Inject constructor() {

    fun mapTrackingData(response: GetLogisticTrackingResponse): TrackingDataModel {
        val data = response.response.data
        return TrackingDataModel().apply {
            trackOrder = mapTrackOrder(data.trackOrder)
            page = mapPage(data.page)
            tipping = mapTippingData(data.tipping)
            lastDriver = mapLastDriverData(data.lastDriver)
        }
    }

    private fun mapLastDriverData(lastDriver: LastDriver): LastDriverModel {
        return LastDriverModel().apply {
            photo = lastDriver.photo
            name =lastDriver.name
            phone = lastDriver.phone
            licenseNumber = lastDriver.licenseNumber
            isChanged = lastDriver.isChanged
        }
    }

    private fun mapTrackOrder(response: TrackOrder): TrackOrderModel {
        return TrackOrderModel().apply {
            detail = mapDetailOrder(response.detail)
            trackHistory = mapTrackingHistory(response.trackHistory)
            change = response.change
            status = response.status
            orderStatus = response.orderStatus
            noHistory = response.noHistory
            receiverName = response.receiverName
            shippingRefNum = response.shippingRefNum
            invalid =  switchInteger(response.invalid)
        }
    }

    private fun mapDetailOrder(detail: Detail): DetailModel {
        return DetailModel().apply {
            shipperCity = detail.shipperCity
            shipperName = detail.shipperName
            receiverCity = detail.receiverCity
            sendDateTime = detail.sendDateTime
            sendDate = detail.sendDate
            sendTime = detail.sendTime
            receiverName = detail.receiverName
            serviceCode = detail.serviceCode
            trackingUrl = detail.trackingUrl
            eta = mapEta(detail.eta)
        }
    }

    private fun mapProofOrder(proof: Proof): ProofModel {
        return ProofModel(imageId = proof.imageId, description = proof.description)
    }

    private fun mapTrackingHistory(trackHistory: List<TrackHistory>): List<TrackHistoryModel> {
        return trackHistory.map {
            TrackHistoryModel(
                    it.dateTime,
                    it.date,
                    it.status,
                    it.city,
                    it.time,
                    it.partnerName,
                    mapProofOrder(it.proof)
            )
        }
    }

    private fun mapPage (page: Page): PageModel {
        return PageModel().apply {
            additionalInfo = mapAdditionalInfo(page.additionalInfo)
        }
    }

    private fun mapAdditionalInfo(additionalInfo: List<AdditionalInfo>): List<AdditionalInfoModel> {
        return additionalInfo.map {
            AdditionalInfoModel(
                    it.title,
                    it.notes,
                    it.urlDetail,
                    it.urlText
            )
        }
    }

    private fun mapTippingData(tipping: Tipping): TippingModel {
        return TippingModel().apply {
            status = tipping.status
            statusTitle = tipping.statusTitle
            statusSubtitle = tipping.statusSubtitle
        }
    }

    private fun switchInteger(value: Int): Boolean {
        return value == 1
    }

    private fun mapEta(eta: Eta) : EtaModel {
        return EtaModel().apply {
            userInfo = eta.userInfo
            userUpdatedInfo = eta.userUpdatedInfo
            isChanged = eta.isUpdated
        }
    }
}

