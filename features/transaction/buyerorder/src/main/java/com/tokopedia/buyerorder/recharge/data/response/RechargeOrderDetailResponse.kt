package com.tokopedia.buyerorder.recharge.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/10/2021
 */
class RechargeOrderDetail(
        @field:SerializedName("status")
        val status: Status = Status(),
        @field:SerializedName("title")
        val title: List<MetaData> = emptyList(),
        @field:SerializedName("metadata")
        val metadata: String = "",
        @field:SerializedName("invoice")
        val invoice: Invoice = Invoice(),
        @field:SerializedName("items")
        val items: List<Item> = emptyList(),
        @field:SerializedName("pricing")
        val pricing: List<MetaData> = emptyList(),
        @field:SerializedName("detail")
        val detail: List<MetaData> = emptyList(),
        @field:SerializedName("paymentData")
        val paymentData: MetaData = MetaData(),
        @field:SerializedName("actionButtons")
        val actionButtons: List<ActionButton> = emptyList(),
        @field:SerializedName("conditionalInfo")
        val conditionalInfo: ConditionalInfo = ConditionalInfo(),
        @field:SerializedName("contactUs")
        val contactUs: ContactUs = ContactUs(),
        @field:SerializedName("paymentMethod")
        val paymentMethod: MetaData = MetaData(),
        @field:SerializedName("payMethod")
        val payMethod: List<MetaData> = emptyList(),
        @field:SerializedName("beforeOrderId")
        val beforeOrderId: String = "0",
        @field:SerializedName("resolutionId")
        val resolutionId: String = "0",
        @field:SerializedName("driverDetails")
        val driverDetails: DriverDetail = DriverDetail(),
        @field:SerializedName("promoCode")
        val promoCode: String = "",
        @field:SerializedName("additionalInfo")
        val additionalInfo: List<MetaData> = emptyList(),
        @field:SerializedName("additionalTickerInfo")
        val additionalTickerInfo: List<AdditionalTickerInfo> = emptyList(),
        @field:SerializedName("purchasedItems")
        val purchasedItems: List<PurchasedItem> = emptyList(),
        @field:SerializedName("digitalPaymentInfoMessage")
        val digitalPaymentInfoMessage: DigitalPaymentInfoMessage = DigitalPaymentInfoMessage()
) {
    class Response(
            @field:SerializedName("orderDetails")
            val orderDetails: RechargeOrderDetail = RechargeOrderDetail()
    )
}

class PurchasedItem(
        @field:SerializedName("name")
        val name: String = "",
        @field:SerializedName("price")
        val price: String = ""
)

class Status(
        @field:SerializedName("status")
        val status: Int = 0,
        @field:SerializedName("statusText")
        val statusText: String = "",
        @field:SerializedName("statusLabel")
        val statusLabel: String = "",
        @field:SerializedName("iconUrl")
        val iconUrl: String = "",
        @field:SerializedName("textColor")
        val textColor: String = "",
        @field:SerializedName("backgroundColor")
        val backgroundColor: String = "",
        @field:SerializedName("fontSize")
        val fontSize: String = ""
)

class MetaData(
        @field:SerializedName("label")
        val label: String = "",
        @field:SerializedName("value")
        val value: String = "",
        @field:SerializedName("textColor")
        val textColor: String = "",
        @field:SerializedName("backgroundColor")
        val backgroundColor: String = "",
        @field:SerializedName("imageUrl")
        val imageUrl: String = ""
)

class Invoice(
        @field:SerializedName("invoiceRefNum")
        val invoiceRefNum: String = "",
        @field:SerializedName("invoiceUrl")
        val invoiceUrl: String = ""
)

class Item(
        @field:SerializedName("title")
        val title: String = "",
        @SuppressLint("Invalid Data Type")
        @field:SerializedName("price")
        val price: String = "",
        @field:SerializedName("totalPrice")
        val totalPrice: String = "",
        @field:SerializedName("categoryID")
        val categoryId: String = "0",
        @field:SerializedName("id")
        val id: String = "",
        @field:SerializedName("imageUrl")
        val imageUrl: String = "",
        @field:SerializedName("category")
        val category: String = "",
        @field:SerializedName("quantity")
        val quantity: Int = 0,
        @field:SerializedName("trackingURL")
        val trackingUrl: String = "",
        @field:SerializedName("trackingNumber")
        val trackingNumber: String = "",
        @field:SerializedName("invoiceID")
        val invoiceId: String = "0",
        @field:SerializedName("promotionAmount")
        val promotionAmount: String = "",
        @field:SerializedName("metaData")
        val metaData: String = "",
        @field:SerializedName("actionButtons")
        val actionButtons: List<ActionButton> = emptyList(),
        @field:SerializedName("tapActions")
        val tapActions: List<ActionButton> = emptyList()
)

class ActionButton(
        @field:SerializedName("label")
        val label: String = "",
        @field:SerializedName("buttonType")
        val buttonType: String = "",
        @field:SerializedName("uri")
        val uri: String = "",
        @field:SerializedName("mappingUri")
        val mappingUri: String = "",
        @field:SerializedName("weight")
        val weight: Int = 0,
        @field:SerializedName("key")
        val key: String = "",
        @field:SerializedName("method")
        val method: String = "",
        @field:SerializedName("uriWeb")
        val uriWeb: String = "",
        @field:SerializedName("value")
        val value: String = "",
        @field:SerializedName("name")
        val name: String = "",
        @field:SerializedName("control")
        val control: String = "",
        @field:SerializedName("header")
        val header: String = "",
        @field:SerializedName("color")
        val color: ActionColor = ActionColor(),
        @field:SerializedName("body")
        val body: ActionBody = ActionBody()
)

class ActionColor(
        @field:SerializedName("textColor")
        val textColor: String = "",
        @field:SerializedName("border")
        val border: String = "",
        @field:SerializedName("background")
        val background: String = ""
)

class ActionBody(
        @field:SerializedName("appURL")
        val appUrl: String = "",
        @field:SerializedName("webURL")
        val webUrl: String = "",
        @field:SerializedName("method")
        val method: String = "",
        @field:SerializedName("body")
        val body: String = ""
)

class ConditionalInfo(
        @field:SerializedName("text")
        val text: String = "",
        @field:SerializedName("color")
        val color: ActionColor = ActionColor(),
        @field:SerializedName("url")
        val url: String = ""
)

class ContactUs(
        @field:SerializedName("helpText")
        val helpText: String = "",
        @field:SerializedName("helpUrl")
        val helpUrl: String = ""
)

class DriverDetail(
        @field:SerializedName("name")
        val name: String = "",
        @field:SerializedName("phone")
        val phone: String = "",
        @field:SerializedName("photo")
        val photo: String = "",
        @field:SerializedName("licenseNumber")
        val licenseNumber: String = "",
        @field:SerializedName("trackingURL")
        val trackingURL: String = ""
)

class AdditionalTickerInfo(
        @field:SerializedName("title")
        val title: String = "",
        @field:SerializedName("notes")
        val notes: String = "",
        @field:SerializedName("urlDetail")
        val urlDetail: String = "",
        @field:SerializedName("urlText")
        val urlText: String = ""
)
data class DigitalPaymentInfoMessage(
    @field:SerializedName("message")
    val message: String = "",
    @field:SerializedName("urlText")
    val urlText: String = "",
    @field:SerializedName("appLink")
    val appLink: String = "",
    @field:SerializedName("webLink")
    val webLink: String = "",
)
