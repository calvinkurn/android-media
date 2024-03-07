package com.tokopedia.logisticorder.uimodel

import com.tokopedia.logisticorder.utils.TippingConstant
import com.tokopedia.targetedticker.domain.TargetedTickerParamModel

data class TrackingPageModel(
    var trackingDataModel: TrackingDataModel = TrackingDataModel()
)

data class TrackingDataModel(
    var trackOrder: TrackOrderModel = TrackOrderModel(),
    var page: PageModel = PageModel(),
    var tipping: TippingModel = TippingModel(),
    var lastDriver: LastDriverModel = LastDriverModel()
)

data class TrackOrderModel(
    var detail: DetailModel = DetailModel(),
    var trackHistory: List<TrackHistoryModel> = listOf(),
    var change: Int = -1,
    var status: String = "",
    var orderStatus: Int = -1,
    var noHistory: Int = -1,
    var receiverName: String = "",
    var shippingRefNum: String = "",
    var invalid: Boolean = false
)

data class DetailModel(
    var shipperCity: String = "",
    var shipperName: String = "",
    var receiverCity: String = "",
    var sendDateTime: String = "",
    var sendDate: String = "",
    var sendTime: String = "",
    var receiverName: String = "",
    var serviceCode: String = "",
    var trackingUrl: String = "",
    var eta: EtaModel = EtaModel()
)

data class ProofModel(
    var imageId: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var accessToken: String = ""
)

data class TrackHistoryModel(
    var dateTime: String = "",
    var date: String = "",
    var status: String = "",
    var city: String = "",
    var time: String = "",
    var partnerName: String = "",
    var proof: ProofModel = ProofModel()
)

data class PageModel(
    var additionalInfo: List<AdditionalInfoModel> = listOf(),
    var contactUsUrl: String = "",
    var targetedTickerParam: TargetedTickerParamModel = TargetedTickerParamModel()
)

data class AdditionalInfoModel(
    var title: String = "",
    var notes: String = "",
    var urlDetail: String = "",
    var urlText: String = ""
)

data class EtaModel(
    var userInfo: String = "",
    var userUpdatedInfo: String = "",
    var isChanged: Boolean = false
)

data class TippingModel(
    var status: Int = 0,
    var statusTitle: String = "",
    var statusSubtitle: String = "",
    var refNumber: String = ""
) {
    val eligibleForTipping: Boolean
        get() = status == TippingConstant.OPEN || status == TippingConstant.WAITING_PAYMENT || status == TippingConstant.SUCCESS_PAYMENT || status == TippingConstant.SUCCESS_TIPPING || status == TippingConstant.REFUND_TIP
}

data class LastDriverModel(
    var photo: String = "",
    var name: String = "",
    var phone: String = "",
    var licenseNumber: String = "",
    var isChanged: Boolean = false
)
