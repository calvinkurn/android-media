package com.tokopedia.entertainment.pdp.data.pdp

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class EventVerifyResponseV2(
        @SerializedName("event_verify")
        @Expose
        val eventVerify: EventVerifyResponse = EventVerifyResponse()
)

data class EventVerifyResponse(
        @SerializedName("error")
        @Expose
        val error: String = "",
        @SerializedName("error_description")
        @Expose
        val errorDescription: String = "",
        @SerializedName("metadata")
        @Expose
        val metadata: MetaDataResponse = MetaDataResponse(),
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("gateway_code")
        @Expose
        val gatewayCode: String = ""
)

@Parcelize
data class MetaDataResponse(
        @SerializedName("category_name")
        @Expose
        val categoryName: String = "",
        @SerializedName("error")
        @Expose
        val error: String = "",
        @SerializedName("item_ids")
        @Expose
        val itemIds: List<String> = arrayListOf(),
        @SerializedName("item_map")
        @Expose
        val itemMap: List<ItemMapResponse> = arrayListOf(),
        @SerializedName("order_subTitle")
        @Expose
        val orderSubTitle: String = "",
        @SerializedName("order_title")
        @Expose
        val orderTitle: String = "",
        @SerializedName("product_ids")
        @Expose
        val productIds: List<String> = arrayListOf(),
        @SerializedName("product_names")
        @Expose
        val productNames: List<String> = arrayListOf(),
        @SerializedName("provider_ids")
        @Expose
        val providerIds: List<String> = arrayListOf(),
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,
        @SerializedName("total_price")
        @Expose
        val totalPrice: Int = 0

) : Parcelable

@Parcelize
data class ItemMapResponse(
        @SerializedName("base_price")
        @Expose
        val basePrice:String = "",
        @SerializedName("category_id")
        @Expose
        val categoryId:String = "",
        @SerializedName("child_category_ids")
        @Expose
        val childCategoryIds:String = "",
        @SerializedName("commission")
        @Expose
        val commission: Int = 0,
        @SerializedName("commission_type")
        @Expose
        val commissionType:String = "",
        @SerializedName("currency_price")
        @Expose
        val currencyPrice: Int = 0,
        @SerializedName("description")
        @Expose
        val description:String = "",
        @SerializedName("email")
        @Expose
        val email:String = "",
        @SerializedName("end_time")
        @Expose
        val endTime:String = "",
        @SerializedName("error")
        @Expose
        val error:String = "",
        @SerializedName("flag_id")
        @Expose
        val flagId:String = "",
        @SerializedName("id")
        @Expose
        val id:String = "",
        @SerializedName("invoice_id")
        @Expose
        val invoiceId:String = "",
        @SerializedName("invoice_item_id")
        @Expose
        val invoiceItemId:String = "",
        @SerializedName("invoice_status")
        @Expose
        val invoiceStatus:String = "",
        @SerializedName("location_desc")
        @Expose
        val locationDesc:String = "",
        @SerializedName("location_name")
        @Expose
        val locationName:String = "",
        @SerializedName("mobile")
        @Expose
        val mobile:String = "",
        @SerializedName("name")
        @Expose
        val name:String = "",
        @SerializedName("order_trace_id")
        @Expose
        val orderTraceId:String = "",
        @SerializedName("package_id")
        @Expose
        val packageId:String = "",
        @SerializedName("package_name")
        @Expose
        val packageName:String = "",
        @SerializedName("payment_type")
        @Expose
        val paymentType:String = "",
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("product_app_url")
        @Expose
        val productAppUrl:String = "",
        @SerializedName("product_id")
        @Expose
        val productId:String = "",
        @SerializedName("product_image")
        @Expose
        val productImage:String = "",
        @SerializedName("product_name")
        @Expose
        val productName:String = "",
        @SerializedName("provider_id")
        @Expose
        val providerId:String = "",
        @SerializedName("provider_invoice_code")
        @Expose
        val providerInvoiceCode:String = "",
        @SerializedName("provider_order_id")
        @Expose
        val providerOrderId:String = "",
        @SerializedName("provider_package_id")
        @Expose
        val providerPackageId:String = "",
        @SerializedName("provider_schedule_id")
        @Expose
        val providerScheduleId:String = "",
        @SerializedName("provider_ticket_id")
        @Expose
        val providerTicketId:String = "",
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,
        @SerializedName("schedule_timestamp")
        @Expose
        val scheduleTimestamp:String = "",
        @SerializedName("start_time")
        @Expose
        val startTime:String = "",
        @SerializedName("total_price")
        @Expose
        val totalPrice: Int = 0,
        @SerializedName("web_app_url")
        @Expose
        val webAppUrl:String = "",
        @SerializedName("product_web_url")
        @Expose
        val productWebUrl:String = "",
        @SerializedName("passenger_forms")
        @Expose
        var passengerForms : MutableList<PassengerForm> = arrayListOf()
) : Parcelable

@Parcelize
data class PassengerForm(
        @SerializedName("passenger_informations")
        @Expose
        var passengerInformation : List<PassengerInformation> = arrayListOf()
) : Parcelable

@Parcelize
data class PassengerInformation(
        @SerializedName("name")
        @Expose
        var name : String = "",
        @SerializedName("value")
        @Expose
        var value : String = "",
        @SerializedName("title")
        @Expose
        var title : String = ""

) : Parcelable