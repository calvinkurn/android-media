package com.tokopedia.common_entertainment.data

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DealsMetaDataCheckout(
    @SerializedName("category_name")
    @Expose
    val categoryName: String = "",
    @SerializedName("error")
    @Expose
    val error: String = "",
    @SerializedName("item_ids")
    @Expose
    val itemIds: List<Int> = emptyList(),
    @SerializedName("item_map")
    @Expose
    val itemMap: List<ItemMapCheckout> = emptyList(),
    @SerializedName("order_subTitle")
    @Expose
    val orderSubTitle: String = "",
    @SerializedName("order_title")
    @Expose
    val orderTitle: String = "",
    @SerializedName("product_ids")
    @Expose
    val productIds: List<Int> = emptyList(),
    @SerializedName("product_names")
    @Expose
    val productNames: List<String> = emptyList(),
    @SerializedName("provider_ids")
    @Expose
    val providerIds: List<Int> = emptyList(),
    @SerializedName("quantity")
    @Expose
    val quantity: Int = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_price")
    @Expose
    val totalPrice: Long = 0
)

data class ItemMapCheckout(
    @SuppressLint("Invalid Data Type")
    @SerializedName("base_price")
    @Expose
    val basePrice:Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("category_id")
    @Expose
    val categoryId: Long = 0,
    @SerializedName("child_category_ids")
    @Expose
    val childCategoryIds:String = "",
    @SerializedName("commission")
    @Expose
    val commission: Int = 0,
    @SerializedName("commission_type")
    @Expose
    val commissionType:String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("currency_price")
    @Expose
    val currencyPrice: Long = 0,
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
    @SuppressLint("Invalid Data Type")
    @SerializedName("flag_id")
    @Expose
    val flagId:Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    @Expose
    val id:Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("invoice_id")
    @Expose
    val invoiceId:Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("invoice_item_id")
    @Expose
    val invoiceItemId:Long = 0,
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
    @SuppressLint("Invalid Data Type")
    @SerializedName("package_id")
    @Expose
    val packageId:Long = 0,
    @SerializedName("package_name")
    @Expose
    val packageName:String = "",
    @SerializedName("payment_type")
    @Expose
    val paymentType:String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    @Expose
    val price: Long = 0,
    @SerializedName("product_app_url")
    @Expose
    val productAppUrl:String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("product_id")
    @Expose
    val productId: Long = 0,
    @SerializedName("product_image")
    @Expose
    val productImage:String = "",
    @SerializedName("product_name")
    @Expose
    val productName:String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("provider_id")
    @Expose
    val providerId:Long = 0,
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
    val scheduleTimestamp:Int = 0,
    @SerializedName("start_time")
    @Expose
    val startTime:String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_price")
    @Expose
    val totalPrice: Long = 0,
    @SerializedName("product_web_url")
    @Expose
    val productWebUrl:String = "",
    @SerializedName("passenger_forms")
    @Expose
    var passengerForms : List<PassengerForm> = emptyList()
)
