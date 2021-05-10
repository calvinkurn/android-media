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
        }
    }

    fun mapTrackOrder(response: TrackOrder): TrackOrderModel {
        return TrackOrderModel().apply {
            detail = mapDetailOrder(response.detail)
            trackHistory = mapTrackingHistory(response.trackHistory)
            change = response.change
            status = response.status
            orderStatus = response.orderStatus
            noHistory = response.noHistory
            receiverName = response.receiverName
            shippingRefNum = response.shippingRefNum
            invalid =  response.invalid
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
            proof = mapProofOrder(detail.proof)
        }
    }

    private fun mapProofOrder(proof: Proof): ProofModel {
        return ProofModel(imageId = proof.imageId)
    }

    private fun mapTrackingHistory(trackHistory: List<TrackHistory>): List<TrackHistoryModel> {
        return trackHistory.map {
            TrackHistoryModel(
                    it.dateTime,
                    it.date,
                    it.status,
                    it.city,
                    it.time
            )
        }
    }

    private fun mapPage (page: Page): PageModel {
        return PageModel().apply {
            additionalInfo = mapAdditionalInfo(page.additionalInfo)
        }
    }

    private fun mapAdditionalInfo(additionalInfo: AdditionalInfo): AdditionalInfoModel {
        return AdditionalInfoModel().apply {
            title = additionalInfo.title
            notes = additionalInfo.notes
            urlDetail = additionalInfo.urlDetail
            urlText = additionalInfo.urlText
        }
    }
}

