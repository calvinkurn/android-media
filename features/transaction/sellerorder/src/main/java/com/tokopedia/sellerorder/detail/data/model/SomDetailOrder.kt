package com.tokopedia.sellerorder.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-08-27.
 */
data class SomDetailOrder (
    @SerializedName("data")
    @Expose
    val data: Data = Data()) {

    data class Data (
         @SerializedName("get_som_detail")
         @Expose
         val getSomDetail: GetSomDetail = GetSomDetail()) {

        data class GetSomDetail (
            @SerializedName("order_id")
            @Expose
            val orderId: Int = 0,

            @SerializedName("status")
            @Expose
            val statusId: Int = 0,

            @SerializedName("status_text")
            @Expose
            val statusText: String = "",

            @SerializedName("invoice")
            @Expose
            val invoice: String = "",

            @SerializedName("invoice_url")
            @Expose
            val invoiceUrl: String = "",

            @SerializedName("checkout_date")
            @Expose
            val checkoutDate: String = "",

            @SerializedName("payment_date")
            @Expose
            val paymentDate: String = "",

            @SerializedName("notes")
            @Expose
            val notes: String = "",

            @SerializedName("products")
            @Expose
            val listProduct: List<Products> = listOf(),

            @SerializedName("customer")
            @Expose
            val customer: Customer = Customer(),

            @SerializedName("shipment")
            @Expose
            val shipment: Shipment = Shipment(),

            @SerializedName("booking_info")
            @Expose
            val bookingInfo: BookingInfo = BookingInfo(),

            @SerializedName("receiver")
            @Expose
            val receiver: Receiver = Receiver(),

            @SerializedName("deadline")
            @Expose
            val deadline: Deadline = Deadline(),

            @SerializedName("insurance")
            @Expose
            val insurance: Insurance = Insurance(),

            @SerializedName("label_info")
            @Expose
            val listLabelInfo: List<LabelInfo> = listOf(),

            @SerializedName("flag_order_meta")
            @Expose
            val flagOrderMeta: FlagOrderMeta = FlagOrderMeta(),

            @SerializedName("payment_method")
            @Expose
            val paymentMethod: List<PaymentMethod> = listOf(),

            @SerializedName("payment_summary")
            @Expose
            val paymentSummary: PaymentSummary = PaymentSummary(),

            @SerializedName("button")
            @Expose
            val button: List<Button> = listOf()) {

            data class Products(
                    @SerializedName("id")
                    @Expose
                    val id: Int = 0,

                    @SerializedName("order_detail_id")
                    @Expose
                    val orderDetailId: Int = 0,

                    @SerializedName("name")
                    @Expose
                    val name: String = "",

                    @SerializedName("product_url")
                    @Expose
                    val productUrl: String = "",

                    @SerializedName("thumbnail")
                    @Expose
                    val thumbnail: String = "",

                    @SerializedName("price_text")
                    @Expose
                    val priceText: String = "",

                    @SerializedName("weight_text")
                    @Expose
                    val weightText: String = "",

                    @SerializedName("quantity")
                    @Expose
                    val quantity: Int = 0,

                    @SerializedName("note")
                    @Expose
                    val note: String = "",

                    @SerializedName("free_return_message")
                    @Expose
                    val freeReturnMessage: String = "",

                    @SerializedName("purchase_protection_fee_text")
                    @Expose
                    val purchaseProtectionFeeText: String = "",

                    @SerializedName("purchase_protection_quantity")
                    @Expose
                    val purchaseProtectionQuantity: Int = 0)

            data class Customer(
                    @SerializedName("id")
                    @Expose
                    val id: Int = 0,

                    @SerializedName("name")
                    @Expose
                    val name: String = "",

                    @SerializedName("image")
                    @Expose
                    val image: String = "",

                    @SerializedName("phone")
                    @Expose
                    val phone: String = "")

            data class Shipment(
                    @SerializedName("id")
                    @Expose
                    val id: Int = 0,

                    @SerializedName("name")
                    @Expose
                    val name: String = "",

                    @SerializedName("product_id")
                    @Expose
                    val productId: Int = 0,

                    @SerializedName("product_name")
                    @Expose
                    val productName: String = "",

                    @SerializedName("is_same_day")
                    @Expose
                    val isSameDay: Int = 0,

                    @SerializedName("awb")
                    @Expose
                    val awb: String = "",

                    @SerializedName("awb_text_color")
                    @Expose
                    val awbTextColor: String = "",

                    @SerializedName("awb_upload_url")
                    @Expose
                    val awbUploadUrl: String = "",

                    @SerializedName("awb_upload_proof_text")
                    @Expose
                    val awbUploadProofText: String = "")

            data class BookingInfo(
                    @SerializedName("driver")
                    @Expose
                    val driver: Driver = Driver(),

                    @SerializedName("online_booking")
                    @Expose
                    val onlineBooking: OnlineBooking = OnlineBooking()) {

                data class Driver(
                        @SerializedName("name")
                        @Expose
                        val name: String = "",

                        @SerializedName("phone")
                        @Expose
                        val phone: String = "",

                        @SerializedName("photo")
                        @Expose
                        val photo: String = "",

                        @SerializedName("license_number")
                        @Expose
                        val licenseNumber: String = "",

                        @SerializedName("tracking_url")
                        @Expose
                        val trackingUrl: String = ""
                )

                data class OnlineBooking(
                        @SerializedName("booking_code")
                        @Expose
                        val bookingCode: String = "",

                        @SerializedName("state")
                        @Expose
                        val state: Int = -1,

                        @SerializedName("message")
                        @Expose
                        val message: String = "",

                        @SerializedName("barcode_type")
                        @Expose
                        val barcodeType: String = ""
                )
            }

            data class Receiver(
                    @SerializedName("name")
                    @Expose
                    val name: String = "",

                    @SerializedName("phone")
                    @Expose
                    val phone: String = "",

                    @SerializedName("street")
                    @Expose
                    val street: String = "",

                    @SerializedName("postal")
                    @Expose
                    val postal: String = "",

                    @SerializedName("district")
                    @Expose
                    val district: String = "",

                    @SerializedName("city")
                    @Expose
                    val city: String = "",

                    @SerializedName("province")
                    @Expose
                    val province: String = "")

            data class Deadline(
                    @SerializedName("text")
                    @Expose
                    val text: String = "",

                    @SerializedName("color")
                    @Expose
                    val color: String = "")

            data class Insurance(
                    @SerializedName("type")
                    @Expose
                    val text: Int = 0,

                    @SerializedName("name")
                    @Expose
                    val name: String = "",

                    @SerializedName("note")
                    @Expose
                    val note: String = "")

            data class LabelInfo(
                    @SerializedName("flag_name")
                    @Expose
                    val flagName: String = "",

                    @SerializedName("flag_color")
                    @Expose
                    val flagColor: String = "",

                    @SerializedName("flag_background")
                    @Expose
                    val flagBg: String = "")

            data class FlagOrderMeta(
                    @SerializedName("is_free_shipping_campaign")
                    @Expose
                    val flagFreeShipping: Boolean = false)

            data class PaymentMethod(
                    @SerializedName("gateway_id")
                    @Expose
                    val gatewayId: Int = 0,

                    @SerializedName("gateway_name")
                    @Expose
                    val gatewayName: String = "",

                    @SerializedName("gateway_url")
                    @Expose
                    val gatewayUrl: String = "")

            data class PaymentSummary(
                    @SerializedName("products_price_text")
                    @Expose
                    val productsPriceText: String = "",

                    @SerializedName("shipping_price_text")
                    @Expose
                    val shippingPriceText: String = "",

                    @SerializedName("insurance_price")
                    @Expose
                    val insurancePrice: Int = 0,

                    @SerializedName("insurance_price_text")
                    @Expose
                    val insurancePriceText: String = "",

                    @SerializedName("additional_price")
                    @Expose
                    val additionalPrice: Int = 0,

                    @SerializedName("additional_price_text")
                    @Expose
                    val additionalPriceText: String = "",

                    @SerializedName("total_item")
                    @Expose
                    val totalItem: Int = 0,

                    @SerializedName("total_weight_text")
                    @Expose
                    val totalWeightText: String = "",

                    @SerializedName("total_price_text")
                    @Expose
                    val totalPriceText: String = "")

            data class Button(
                    @SerializedName("key")
                    @Expose
                    val key: String = "",

                    @SerializedName("display_name")
                    @Expose
                    val displayName: String = "",

                    @SerializedName("color")
                    @Expose
                    val color: String = "",

                    @SerializedName("type")
                    @Expose
                    val type: String = "",

                    @SerializedName("url")
                    @Expose
                    val url: String = "",

                    @SerializedName("title")
                    @Expose
                    val title: String = "",

                    @SerializedName("content")
                    @Expose
                    val content: String = "",

                    @SerializedName("param")
                    @Expose
                    val param: String = "")
        }
    }
}