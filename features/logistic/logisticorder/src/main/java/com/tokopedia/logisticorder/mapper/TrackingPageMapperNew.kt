package com.tokopedia.logisticorder.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper.getDeliveryImage
import com.tokopedia.logisticorder.domain.response.AdditionalInfo
import com.tokopedia.logisticorder.domain.response.Detail
import com.tokopedia.logisticorder.domain.response.Eta
import com.tokopedia.logisticorder.domain.response.GetLogisticTrackingResponse
import com.tokopedia.logisticorder.domain.response.LastDriver
import com.tokopedia.logisticorder.domain.response.Page
import com.tokopedia.logisticorder.domain.response.Proof
import com.tokopedia.logisticorder.domain.response.Tipping
import com.tokopedia.logisticorder.domain.response.TrackHistory
import com.tokopedia.logisticorder.domain.response.TrackOrder
import com.tokopedia.logisticorder.uimodel.AdditionalInfoModel
import com.tokopedia.logisticorder.uimodel.DetailModel
import com.tokopedia.logisticorder.uimodel.EtaModel
import com.tokopedia.logisticorder.uimodel.LastDriverModel
import com.tokopedia.logisticorder.uimodel.PageModel
import com.tokopedia.logisticorder.uimodel.ProofModel
import com.tokopedia.logisticorder.uimodel.TickerUnificationTargets
import com.tokopedia.logisticorder.uimodel.TippingModel
import com.tokopedia.logisticorder.uimodel.TrackHistoryModel
import com.tokopedia.logisticorder.uimodel.TrackOrderModel
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import javax.inject.Inject

class TrackingPageMapperNew @Inject constructor() {

    fun mapTrackingData(response: GetLogisticTrackingResponse): TrackingDataModel {
        val data = response.response.data
        return TrackingDataModel().apply {
            trackOrder = mapTrackOrder(data.trackOrder)
            page = mapPage(data.page)
            tipping = mapTippingData(data.tipping, data.trackOrder.shippingRefNum)
            lastDriver = mapLastDriverData(data.lastDriver)
        }
    }

    fun mapTrackingDataCompose(
        response: GetLogisticTrackingResponse,
        userId: String,
        deviceId: String,
        orderId: String?,
        trackingUrlFromOrder: String?,
        accessToken: String?
    ): TrackingDataModel {
        val data = response.response.data
        return TrackingDataModel().apply {
            trackOrder = mapTrackOrder(data.trackOrder, userId, deviceId, orderId, trackingUrlFromOrder, accessToken)
            page = mapPage(data.page)
            tipping = mapTippingData(data.tipping, data.trackOrder.shippingRefNum)
            lastDriver = mapLastDriverData(data.lastDriver)
        }
    }

    private fun mapLastDriverData(lastDriver: LastDriver): LastDriverModel {
        return LastDriverModel().apply {
            photo = lastDriver.photo
            name = lastDriver.name
            phone = lastDriver.phone
            licenseNumber = lastDriver.licenseNumber
            isChanged = lastDriver.isChanged
        }
    }

    private fun mapTrackOrder(
        response: TrackOrder,
        userId: String? = null,
        deviceId: String? = null,
        orderId: String? = null,
        trackingUrlFromOrder: String? = null,
        accessToken: String? = null
    ): TrackOrderModel {
        return TrackOrderModel().apply {
            detail = mapDetailOrder(response.detail, trackingUrlFromOrder)
            trackHistory = mapTrackingHistory(response.trackHistory, userId, deviceId, orderId, accessToken)
            change = response.change
            status = response.status
            orderStatus = response.orderStatus
            noHistory = response.noHistory
            receiverName = response.receiverName
            shippingRefNum = response.shippingRefNum
            invalid = switchInteger(response.invalid)
        }
    }

    private fun mapDetailOrder(detail: Detail, trackingUrlFromOrder: String?): DetailModel {
        return DetailModel().apply {
            shipperCity = detail.shipperCity
            shipperName = detail.shipperName
            receiverCity = detail.receiverCity
            sendDateTime = detail.sendDateTime
            sendDate = detail.sendDate
            sendTime = detail.sendTime
            receiverName = detail.receiverName
            serviceCode = detail.serviceCode
            trackingUrl = trackingUrlFromOrder?.takeIf { it.isNotEmpty() } ?: detail.trackingUrl
            eta = mapEta(detail.eta)
        }
    }

    private fun mapProofOrder(
        proof: Proof,
        userId: String?,
        deviceId: String?,
        orderId: String?,
        accessToken: String?
    ): ProofModel {
        val imageUrl =
            if (proof.imageId.isNotEmpty() && !userId.isNullOrEmpty() && !deviceId.isNullOrEmpty() && !orderId.isNullOrEmpty()) {
                getDeliveryImage(
                    proof.imageId,
                    orderId.toLongOrZero(),
                    LogisticImageDeliveryHelper.IMAGE_SMALL_SIZE,
                    userId,
                    LogisticImageDeliveryHelper.DEFAULT_OS_TYPE,
                    deviceId
                )
            } else {
                ""
            }
        return ProofModel(
            imageId = proof.imageId,
            description = proof.description,
            imageUrl = imageUrl,
            accessToken = accessToken.orEmpty()
        )
    }

    private fun mapTrackingHistory(
        trackHistory: List<TrackHistory>,
        userId: String?,
        deviceId: String?,
        orderId: String?,
        accessToken: String?
    ): List<TrackHistoryModel> {
        return trackHistory.map {
            TrackHistoryModel(
                it.dateTime,
                it.date,
                it.status,
                it.city,
                it.time,
                it.partnerName,
                mapProofOrder(it.proof, userId, deviceId, orderId, accessToken)
            )
        }
    }

    private fun mapPage(page: Page): PageModel {
        return PageModel().apply {
            additionalInfo = mapAdditionalInfo(page.additionalInfo)
            contactUsUrl = page.helpPageUrl
            tickerUnificationTargets = page.tickerUnificationTargets.map {
                TickerUnificationTargets(
                    it.type,
                    it.values
                )
            }
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

    private fun mapTippingData(tipping: Tipping, shippingRefNum: String): TippingModel {
        return TippingModel().apply {
            status = tipping.status
            statusTitle = tipping.statusTitle
            statusSubtitle = tipping.statusSubtitle
            refNumber = shippingRefNum
        }
    }

    private fun switchInteger(value: Int): Boolean {
        return value == 1
    }

    private fun mapEta(eta: Eta): EtaModel {
        return EtaModel().apply {
            userInfo = eta.userInfo
            userUpdatedInfo = eta.userUpdatedInfo
            isChanged = eta.isUpdated
        }
    }
}
