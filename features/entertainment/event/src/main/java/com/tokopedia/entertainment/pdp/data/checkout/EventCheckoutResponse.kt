package com.tokopedia.entertainment.pdp.data.checkout


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventCheckoutResponse(
        @SerializedName("checkout_general_v2")
        @Expose
        val checkout: Checkout = Checkout()
)

data class EventCheckoutInstantResponse(
        @SerializedName("checkout_general_v2_instant")
        @Expose
        val checkout: Checkout = Checkout()
)

data class Checkout(
        @SerializedName("data")
        @Expose
        val data: Data = Data(),
        @SerializedName("error_reporter")
        @Expose
        val errorReporter: ErrorReporter = ErrorReporter(),
        @SerializedName("header")
        @Expose
        val header: Header = Header()
)

data class Data(
        @SerializedName("data")
        @Expose
        val data: DataX = DataX(),
        @SerializedName("error")
        @Expose
        val error: String = "",
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("success")
        @Expose
        val success: Int = 0
)

data class DataX(
        @SerializedName("callback_url")
        @Expose
        val callbackUrl: String = "",
        @SerializedName("parameter")
        @Expose
        val parameter: Parameter = Parameter(),
        @SerializedName("price_validation")
        @Expose
        val priceValidation: PriceValidation = PriceValidation(),
        @SerializedName("product_list")
        @Expose
        val productList: List<Product> = emptyList(),
        @SerializedName("query_string")
        @Expose
        val queryString: String = "",
        @SerializedName("redirect_url")
        @Expose
        val redirectUrl: String = ""
)

data class Parameter(
        @SerializedName("amount")
        @Expose
        val amount: Int = 0,
        @SerializedName("currency")
        @Expose
        val currency: String = "",
        @SerializedName("customer_email")
        @Expose
        val customerEmail: String = "",
        @SerializedName("customer_id")
        @Expose
        val customerId: Int = 0,
        @SerializedName("customer_msisdn")
        @Expose
        val customerMsisdn: String = "",
        @SerializedName("customer_name")
        @Expose
        val customerName: String = "",
        @SerializedName("device_info")
        @Expose
        val deviceInfo: DeviceInfo = DeviceInfo(),
        @SerializedName("gateway_code")
        @Expose
        val gatewayCode: String = "",
        @SerializedName("language")
        @Expose
        val language: String = "",
        @SerializedName("merchant_code")
        @Expose
        val merchantCode: String = "",
        @SerializedName("merchant_type")
        @Expose
        val merchantType: String = "",
        @SerializedName("nid")
        @Expose
        val nid: String = "",
        @SerializedName("payment_metadata")
        @Expose
        val paymentMetadata: String = "",
        @SerializedName("pid")
        @Expose
        val pid: String = "",
        @SerializedName("profile_code")
        @Expose
        val profileCode: String = "",
        @SerializedName("signature")
        @Expose
        val signature: String = "",
        @SerializedName("transaction_date")
        @Expose
        val transactionDate: String = "",
        @SerializedName("transaction_id")
        @Expose
        val transactionId: String = "",
        @SerializedName("user_defined_value")
        @Expose
        val userDefinedValue: String = ""
)

data class DeviceInfo(
        @SerializedName("device_name")
        @Expose
        val deviceName: String = "",
        @SerializedName("device_version")
        @Expose
        val deviceVersion: String = ""
)

data class PriceValidation(
        @SerializedName("is_updated")
        @Expose
        val isUpdated: Boolean = false,
        @SerializedName("message")
        @Expose
        val message: Message = Message(),
        @SerializedName("tracker_data")
        @Expose
        val trackerData: TrackerData = TrackerData()
)

data class Product(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("price")
        @Expose
        val price: Int,
        @SerializedName("quantity")
        @Expose
        val quantity: Int
)

data class Message(
        @SerializedName("action")
        @Expose
        val action: String = "",
        @SerializedName("desc")
        @Expose
        val desc: String = "",
        @SerializedName("title")
        @Expose
        val title: String = ""
)

data class ErrorReporter(
        @SerializedName("eligible")
        @Expose
        val eligible: Boolean = false,
        @SerializedName("texts")
        @Expose
        val texts: Texts = Texts()
)

data class TrackerData(
        @SerializedName("product_changes_type")
        @Expose
        val productChangesType: String = "",
        @SerializedName("campaign_type")
        @Expose
        val campaignType: String = "",
        @SerializedName("product_ids")
        @Expose
        val productIds: List<String> = emptyList()
)

data class Texts(
        @SerializedName("submit_title")
        @Expose
        val submitTitle: String = "",
        @SerializedName("submit_description")
        @Expose
        val submitDescription: String = "",
        @SerializedName("submit_button")
        @Expose
        val submitButton: String = "",
        @SerializedName("cancel_button")
        @Expose
        val cancelButton: String = ""
)

data class Header(
        @SerializedName("messages")
        @Expose
        val messages: List<String> = emptyList()
)