package com.tokopedia.logisticorder.domain.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetLogisticTrackingResponse(
        @SerializedName("logistic_tracking")
        @Expose
        val response: LogisticTrackingResponse = LogisticTrackingResponse()
)

data class LogisticTrackingResponse(
        @SerializedName("message_error")
        @Expose
        val messageError: List<Any?>? = null,
        @SerializedName("data")
        @Expose
        val data: TrackingData = TrackingData()
)

data class TrackingData(
        @SerializedName("track_order")
        @Expose
        val trackOrder: TrackOrder = TrackOrder(),
        @SerializedName("page")
        @Expose
        val page: Page = Page(),
        @SerializedName("tipping")
        @Expose
        val tipping: Tipping = Tipping(),
        @SerializedName("last_driver")
        @Expose
        val lastDriver: LastDriver = LastDriver()
)

data class TrackOrder(
        @SerializedName("detail")
        @Expose
        val detail: Detail = Detail(),
        @SerializedName("track_history")
        @Expose
        val trackHistory: List<TrackHistory> = listOf(),
        @SerializedName("change")
        @Expose
        val change: Int = -1,
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("order_status")
        @Expose
        val orderStatus: Int = -1,
        @SerializedName("no_history")
        @Expose
        val noHistory: Int = -1,
        @SerializedName("receiver_name")
        @Expose
        val receiverName: String = "",
        @SerializedName("shipping_ref_num")
        @Expose
        val shippingRefNum: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("invalid")
        @Expose
        val invalid: Int = -1
)

data class Detail(
        @SerializedName("shipper_city")
        @Expose
        val shipperCity: String = "",
        @SerializedName("shipper_name")
        @Expose
        val shipperName: String = "",
        @SerializedName("receiver_city")
        @Expose
        val receiverCity: String = "",
        @SerializedName("send_date_time")
        @Expose
        val sendDateTime: String = "",
        @SerializedName("send_date")
        @Expose
        val sendDate: String = "",
        @SerializedName("send_time")
        @Expose
        val sendTime: String = "",
        @SerializedName("receiver_name")
        @Expose
        val receiverName: String = "",
        @SerializedName("service_code")
        @Expose
        val serviceCode: String = "",
        @SerializedName("tracking_url")
        @Expose
        val trackingUrl: String = "",
        @SerializedName("eta")
        @Expose
        val eta: Eta = Eta()

)

data class Proof(
        @SerializedName("image_id")
        @Expose
        val imageId: String = "",
        @SerializedName("copy_writing_disclaimer")
        @Expose
        val description: String = ""
)

data class TrackHistory(
        @SerializedName("date_time")
        @Expose
        val dateTime: String = "",
        @SerializedName("date")
        @Expose
        val date: String = "",
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("city")
        @Expose
        val city: String = "",
        @SerializedName("time")
        @Expose
        val time: String = "",
        @SerializedName("partner_name")
        @Expose
        val partnerName: String = "",
        @SerializedName("proof")
        @Expose
        val proof: Proof = Proof()
)

data class Page(
        @SerializedName("additional_info")
        @Expose
        val additionalInfo: List<AdditionalInfo> = listOf()
)

data class AdditionalInfo(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("notes")
        @Expose
        val notes: String = "",
        @SerializedName("url_detail")
        @Expose
        val urlDetail: String = "",
        @SerializedName("url_text")
        @Expose
        val urlText: String = ""
)

data class Eta(

        @SerializedName("is_updated")
        @Expose
        val isUpdated: Boolean = false,

        @SerializedName("user_info")
        @Expose
        val userInfo: String = "",

        @SerializedName("eta_histories")
        @Expose
        val etaHistories: List<EtaHistoriesItem> = listOf(),

        @SerializedName("triggered_by")
        @Expose
        val triggeredBy: String = "",

        @SerializedName("eta_max")
        @Expose
        val etaMax: String = "",

        @SerializedName("eta_min")
        @Expose
        val etaMin: String = "",

        @SerializedName("event_time")
        @Expose
        val eventTime: String = "",

        @SerializedName("user_updated_info")
        @Expose
        val userUpdatedInfo: String = ""
)

data class EtaHistoriesItem(

        @SerializedName("triggered_by")
        @Expose
        val triggeredBy: String = "",

        @SerializedName("eta_max")
        @Expose
        val etaMax: String = "",

        @SerializedName("eta_min")
        @Expose
        val etaMin: String = "",

        @SerializedName("event_time")
        @Expose
        val eventTime: String = ""
)


data class Tipping(
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("status_title")
        @Expose
        val statusTitle: String = "",
        @SerializedName("status_subtitle")
        @Expose
        val statusSubtitle: String = "",
        @SerializedName("last_driver")
        @Expose
        val tippingLastDriver: TippingLastDriver = TippingLastDriver()
)

data class TippingLastDriver(
        @SerializedName("photo")
        @Expose
        val photo: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("phone")
        @Expose
        val phone: String = "",
        @SerializedName("license_number")
        @Expose
        val licenseNumber: String = "",
        @SerializedName("is_changed")
        @Expose
        val isChanged: Boolean = false
)

data class LastDriver(
        @SerializedName("photo")
        @Expose
        val photo: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("phone")
        @Expose
        val phone: String = "",
        @SerializedName("license_number")
        @Expose
        val licenseNumber: String = "",
        @SerializedName("is_changed")
        @Expose
        val isChanged: Boolean = false
)