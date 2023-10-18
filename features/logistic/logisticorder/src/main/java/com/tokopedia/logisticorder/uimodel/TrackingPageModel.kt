package com.tokopedia.logisticorder.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class TrackingPageModel(
    var trackingDataModel: TrackingDataModel = TrackingDataModel()
)

@Parcelize
data class TrackingDataModel(
    var trackOrder: TrackOrderModel = TrackOrderModel(),
    var page: PageModel = PageModel(),
    var tipping: TippingModel = TippingModel(),
    var lastDriver: LastDriverModel = LastDriverModel()
) : Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
data class ProofModel(
    var imageId: String = "",
    var description: String = ""
) : Parcelable

@Parcelize
data class TrackHistoryModel(
    var dateTime: String = "",
    var date: String = "",
    var status: String = "",
    var city: String = "",
    var time: String = "",
    var partnerName: String = "",
    var proof: ProofModel = ProofModel()
) : Parcelable

@Parcelize
data class PageModel(
    var additionalInfo: List<AdditionalInfoModel> = listOf(),
    var contactUsUrl: String = ""
) : Parcelable

@Parcelize
data class AdditionalInfoModel(
    var title: String = "",
    var notes: String = "",
    var urlDetail: String = "",
    var urlText: String = ""
) : Parcelable

@Parcelize
data class EtaModel(
    var userInfo: String = "",
    var userUpdatedInfo: String = "",
    var isChanged: Boolean = false
) : Parcelable

@Parcelize
data class TippingModel(
    var status: Int = 0,
    var statusTitle: String = "",
    var statusSubtitle: String = ""
) : Parcelable

@Parcelize
data class LastDriverModel(
    var photo: String = "",
    var name: String = "",
    var phone: String = "",
    var licenseNumber: String = "",
    var isChanged: Boolean = false
) : Parcelable
