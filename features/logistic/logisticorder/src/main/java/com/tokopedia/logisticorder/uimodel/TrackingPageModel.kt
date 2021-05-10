package com.tokopedia.logisticorder.uimodel

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticorder.domain.response.*

data class TrackingPageModel(
        var trackingDataModel: TrackingDataModel = TrackingDataModel()
)

data class TrackingDataModel(
        var trackOrder: TrackOrderModel = TrackOrderModel(),
        var page: PageModel = PageModel()
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
        var invalid: Int = -1
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
        var proof: ProofModel = ProofModel()
)

data class ProofModel(
        var imageId: String = ""
)

data class TrackHistoryModel(
        var dateTime: String = "",
        var date: String = "",
        var status: String = "",
        var city: String = "",
        var time: String = ""
)

data class PageModel(
        var additionalInfo: AdditionalInfoModel = AdditionalInfoModel()
)

data class AdditionalInfoModel(
        var title: String = "",
        var notes: String = "",
        var urlDetail: String = "",
        var urlText: String = ""
)