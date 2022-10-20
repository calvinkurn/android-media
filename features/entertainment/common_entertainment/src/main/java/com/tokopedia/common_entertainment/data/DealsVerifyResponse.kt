package com.tokopedia.common_entertainment.data

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DealsVerifyResponse(
    @SerializedName("event_verify")
    val eventVerify: EventVerifyResponse = EventVerifyResponse()
) : Parcelable

@Parcelize
data class EventVerifyResponse(
    @SerializedName("error")
    val error: String = "",
    @SerializedName("error_description")
    val errorDescription: String = "",
    @SerializedName("metadata")
    val metadata: MetaDataResponse = MetaDataResponse(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("gateway_code")
    val gatewayCode: String = ""
) : Parcelable

@Parcelize
data class MetaDataResponse(
    @SerializedName("category_name")
    val categoryName: String = "",
    @SerializedName("error")
    val error: String = "",
    @SerializedName("item_ids")
    val itemIds: List<String> = emptyList(),
    @SerializedName("item_map")
    val itemMap: List<ItemMapResponse> = emptyList(),
    @SerializedName("order_subTitle")
    val orderSubTitle: String = "",
    @SerializedName("order_title")
    val orderTitle: String = "",
    @SerializedName("product_ids")
    val productIds: List<String> = emptyList(),
    @SerializedName("product_names")
    val productNames: List<String> = emptyList(),
    @SerializedName("provider_ids")
    val providerIds: List<String> = emptyList(),
    @SerializedName("quantity")
    val quantity: Int = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_price")
    val totalPrice: Long = 0
) : Parcelable

@Parcelize
data class ItemMapResponse(
    @SerializedName("base_price")
    val basePrice: String = "",
    @SerializedName("category_id")
    val categoryId: String = "",
    @SerializedName("child_category_ids")
    val childCategoryIds: String = "",
    @SerializedName("commission")
    val commission: Int = 0,
    @SerializedName("commission_type")
    val commissionType: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("currency_price")
    val currencyPrice: Long = 0,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("end_time")
    val endTime: String = "",
    @SerializedName("error")
    val error: String = "",
    @SerializedName("flag_id")
    val flagId: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("invoice_id")
    val invoiceId: String = "",
    @SerializedName("invoice_item_id")
    val invoiceItemId: String = "",
    @SerializedName("invoice_status")
    val invoiceStatus: String = "",
    @SerializedName("location_desc")
    val locationDesc: String = "",
    @SerializedName("location_name")
    val locationName: String = "",
    @SerializedName("mobile")
    val mobile: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("order_trace_id")
    val orderTraceId: String = "",
    @SerializedName("package_id")
    val packageId: String = "",
    @SerializedName("package_name")
    val packageName: String = "",
    @SerializedName("payment_type")
    val paymentType: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: Long = 0,
    @SerializedName("product_app_url")
    val productAppUrl: String = "",
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("product_image")
    val productImage: String = "",
    @SerializedName("product_name")
    val productName: String = "",
    @SerializedName("provider_id")
    val providerId: String = "",
    @SerializedName("provider_invoice_code")
    val providerInvoiceCode: String = "",
    @SerializedName("provider_order_id")
    val providerOrderId: String = "",
    @SerializedName("provider_package_id")
    val providerPackageId: String = "",
    @SerializedName("provider_schedule_id")
    val providerScheduleId: String = "",
    @SerializedName("provider_ticket_id")
    val providerTicketId: String = "",
    @SerializedName("quantity")
    val quantity: Int = 0,
    @SerializedName("schedule_timestamp")
    val scheduleTimestamp: String = "",
    @SerializedName("start_time")
    val startTime: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_price")
    val totalPrice: Long = 0,
    @SerializedName("web_app_url")
    val webAppUrl: String = "",
    @SerializedName("product_web_url")
    val productWebUrl: String = "",
    @SerializedName("passenger_forms")
    var passengerForms: List<PassengerForm> = emptyList()
) : Parcelable

@Parcelize
data class PassengerForm(
    @SerializedName("passenger_informations")
    var passengerInformation: List<PassengerInformation> = emptyList()
) : Parcelable

@Parcelize
data class PassengerInformation(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("value")
    var value: String = "",
    @SerializedName("title")
    var title: String = ""
) : Parcelable

