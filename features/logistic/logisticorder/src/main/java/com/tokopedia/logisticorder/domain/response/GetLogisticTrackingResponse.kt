package com.tokopedia.logisticorder.domain.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetLogisticTrackingResponse(
    @SerializedName("logistic_tracking")
    val response: LogisticTrackingResponse = LogisticTrackingResponse()
)

data class LogisticTrackingResponse(
    @SerializedName("message_error")
    val messageError: List<Any?>? = null,
    @SerializedName("data")
    val data: TrackingData = TrackingData()
)

data class TrackingData(
    @SerializedName("track_order")
    val trackOrder: TrackOrder = TrackOrder(),
    @SerializedName("page")
    val page: Page = Page(),
    @SerializedName("tipping")
    val tipping: Tipping = Tipping(),
    @SerializedName("last_driver")
    val lastDriver: LastDriver = LastDriver()
)

data class TrackOrder(
    @SerializedName("detail")
    val detail: Detail = Detail(),
    @SerializedName("track_history")
    val trackHistory: List<TrackHistory> = listOf(),
    @SerializedName("change")
    val change: Int = -1,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("order_status")
    val orderStatus: Int = -1,
    @SerializedName("no_history")
    val noHistory: Int = -1,
    @SerializedName("receiver_name")
    val receiverName: String = "",
    @SerializedName("shipping_ref_num")
    val shippingRefNum: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("invalid")
    val invalid: Int = -1
)

data class Detail(
    @SerializedName("shipper_city")
    val shipperCity: String = "",
    @SerializedName("shipper_name")
    val shipperName: String = "",
    @SerializedName("receiver_city")
    val receiverCity: String = "",
    @SerializedName("send_date_time")
    val sendDateTime: String = "",
    @SerializedName("send_date")
    val sendDate: String = "",
    @SerializedName("send_time")
    val sendTime: String = "",
    @SerializedName("receiver_name")
    val receiverName: String = "",
    @SerializedName("service_code")
    val serviceCode: String = "",
    @SerializedName("tracking_url")
    val trackingUrl: String = "",
    @SerializedName("eta")
    val eta: Eta = Eta()

)

data class Proof(
    @SerializedName("image_id")
    val imageId: String = "",
    @SerializedName("copy_writing_disclaimer")
    val description: String = ""
)

data class TrackHistory(
    @SerializedName("date_time")
    val dateTime: String = "",
    @SerializedName("date")
    val date: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("time")
    val time: String = "",
    @SerializedName("partner_name")
    val partnerName: String = "",
    @SerializedName("proof")
    val proof: Proof = Proof()
)

data class Page(
    @SerializedName("additional_info")
    val additionalInfo: List<AdditionalInfo> = listOf(),
    @SerializedName("help_url_page")
    val helpUrlPage: String = ""
)

data class AdditionalInfo(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("notes")
    val notes: String = "",
    @SerializedName("url_detail")
    val urlDetail: String = "",
    @SerializedName("url_text")
    val urlText: String = ""
)

data class Eta(
    @SerializedName("is_updated")
    val isUpdated: Boolean = false,
    @SerializedName("user_info")
    val userInfo: String = "",
    @SerializedName("eta_histories")
    val etaHistories: List<EtaHistoriesItem> = listOf(),
    @SerializedName("triggered_by")
    val triggeredBy: String = "",
    @SerializedName("eta_max")
    val etaMax: String = "",
    @SerializedName("eta_min")
    val etaMin: String = "",
    @SerializedName("event_time")
    val eventTime: String = "",
    @SerializedName("user_updated_info")
    val userUpdatedInfo: String = ""
)

data class EtaHistoriesItem(
    @SerializedName("triggered_by")
    val triggeredBy: String = "",
    @SerializedName("eta_max")
    val etaMax: String = "",
    @SerializedName("eta_min")
    val etaMin: String = "",
    @SerializedName("event_time")
    val eventTime: String = ""
)

data class Tipping(
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("status_title")
    val statusTitle: String = "",
    @SerializedName("status_subtitle")
    val statusSubtitle: String = "",
    @SerializedName("last_driver")
    @Deprecated("Use TrackingData.lastDriver instead")
    val lastDriver: LastDriver = LastDriver()
)

data class LastDriver(
    @SerializedName("photo")
    val photo: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("license_number")
    val licenseNumber: String = "",
    @SerializedName("is_changed")
    val isChanged: Boolean = false
)
