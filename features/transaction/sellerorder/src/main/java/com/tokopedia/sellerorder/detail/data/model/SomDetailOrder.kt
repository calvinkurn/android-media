package com.tokopedia.sellerorder.detail.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerorder.common.domain.model.TickerInfo
import com.tokopedia.sellerorder.common.presenter.model.PopUp
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 2019-08-27.
 */
data class SomDetailOrder(
        @SerializedName("data")
        @Expose
        val data: Data = Data()) {

    data class Data(
            @SerializedName("get_som_detail")
            @Expose
            val getSomDetail: GetSomDetail = GetSomDetail()) {

        data class GetSomDetail(
                @SerializedName("order_id")
                @Expose
                val orderId: String = "0",

                @SerializedName("status")
                @Expose
                val statusCode: Int = 0,

                @SerializedName("status_text")
                @Expose
                val statusText: String = "",

                @SerializedName("status_text_color")
                @Expose
                val statusTextColor: String = "",

                @SerializedName("status_indicator_color")
                @Expose
                val statusIndicatorColor: String = "",

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

                @SerializedName("dropshipper")
                @Expose
                val dropshipper: Dropshipper = Dropshipper(),

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

                @SerializedName("warehouse")
                @Expose
                val warehouse: Warehouse = Warehouse(),

                @SerializedName("label_info")
                @Expose
                val listLabelInfo: List<LabelInfo> = listOf(),

                @SerializedName("buyer_request_cancel")
                @Expose
                val buyerRequestCancel: BuyerRequestCancel = BuyerRequestCancel(),

                @SerializedName("flag_order_meta")
                @Expose
                val flagOrderMeta: FlagOrderMeta = FlagOrderMeta(),

                @SerializedName("logistic_info")
                @Expose
                val logisticInfo: LogisticInfo = LogisticInfo(),

                @SerializedName("button")
                @Expose
                val button: List<Button> = listOf(),

                @SerializedName("online_booking")
                @Expose
                val onlineBooking: OnlineBookingRoot = OnlineBookingRoot(),

                @SerializedName("penalty_reject_info")
                @Expose
                val penaltyRejectInfo: PenaltyRejectInfo = PenaltyRejectInfo(),

                @SerializedName("ticker_info")
                @Expose
                val tickerInfo: TickerInfo = TickerInfo()) {

            data class Products(
                    @SerializedName("id")
                    @Expose
                    val id: String = "0",

                    @SerializedName("order_detail_id")
                    @Expose
                    val orderDetailId: String = "0",

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
                    val id: String = "0",

                    @SerializedName("name")
                    @Expose
                    val name: String = "",

                    @SerializedName("image")
                    @Expose
                    val image: String = "",

                    @SerializedName("phone")
                    @Expose
                    val phone: String = "")

            data class Dropshipper(
                    @SerializedName("phone")
                    @Expose
                    val phone: String = "",

                    @SerializedName("name")
                    @Expose
                    val name: String = "")

            data class Shipment(
                    @SerializedName("id")
                    @Expose
                    val id: String = "0",

                    @SerializedName("name")
                    @Expose
                    val name: String = "",

                    @SerializedName("product_id")
                    @Expose
                    val productId: String = "0",

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

                        @SerializedName("message_array")
                        @Expose
                        val messageArray: List<String> = arrayListOf(),

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

            data class Warehouse(
                    @SerializedName("warehouse_id")
                    @Expose
                    val warehouseId: String = "0",

                    @SerializedName("fulfill_by")
                    @Expose
                    val fullFillBy: Int = 0)

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

            data class BuyerRequestCancel(
                    @SerializedName("is_request_cancel")
                    @Expose
                    val isRequestCancel: Boolean = false,

                    @SerializedName("request_cancel_time")
                    @Expose
                    val requestCancelTime: String = "",

                    @SerializedName("reason")
                    @Expose
                    val reason: String = "",

                    @SerializedName("status")
                    @Expose
                    val status: Int = 0)

            data class FlagOrderMeta(
                    @SerializedName("is_free_shipping_campaign")
                    @Expose
                    val flagFreeShipping: Boolean = false,

                    @SerializedName("is_topads")
                    @Expose
                    val isTopAds: Boolean = false,

                    @SerializedName("is_tokocabang")
                    @Expose
                    val isWareHouse: Boolean = false,

                    @SerializedName("is_shipping_printed")
                    @Expose
                    val isShippingPrinted: Boolean = false
            )

            data class LogisticInfo(
                    @SerializedName("all")
                    @Expose
                    val logisticInfoAllList: List<All> = listOf(),
                    @SerializedName("priority")
                    @Expose
                    val logisticInfoPriorityList: Priority = Priority(),
                    @SerializedName("others")
                    @Expose
                    val othersLogisticInfoList: List<Others> = listOf()
            ) {
                    @Parcelize
                    data class All(
                            @SerializedName("id")
                            @Expose
                            val id: String? = "",
                            @SerializedName("priority")
                            @Expose
                            val priority: String? = "",
                            @SerializedName("description")
                            @Expose
                            val description: String? = "",
                            @SerializedName("info_text_short")
                            @Expose
                            val infoTextShort: String? = "",
                            @SerializedName("info_text_long")
                            @Expose
                            val infoTextLong: String? = ""
                    ): Parcelable

                    data class Priority(
                            @SerializedName("id")
                            @Expose
                            val id: String? = "",
                            @SerializedName("priority")
                            @Expose
                            val priority: String? = "",
                            @SerializedName("description")
                            @Expose
                            val description: String? = "",
                            @SerializedName("info_text_short")
                            @Expose
                            val infoTextShort: String? = "",
                            @SerializedName("info_text_long")
                            @Expose
                            val infoTextLong: String? = ""
                    )

                    data class Others(
                            @SerializedName("id")
                            @Expose
                            val id: String? = "",
                            @SerializedName("priority")
                            @Expose
                            val priority: String? = "",
                            @SerializedName("description")
                            @Expose
                            val description: String? = "",
                            @SerializedName("info_text_short")
                            @Expose
                            val infoTextShort: String? = "",
                            @SerializedName("info_text_long")
                            @Expose
                            val infoTextLong: String? = ""
                    )
            }

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
                    val param: String = "",

                    @SerializedName("popup")
                    @Expose
                    val popUp: PopUp = PopUp())

            data class OnlineBookingRoot(
                    @SerializedName("is_hide_input_awb")
                    @Expose
                    val isHideInputAwb: Boolean = false,

                    @SerializedName("is_remove_input_awb")
                    @Expose
                    val isRemoveInputAwb: Boolean = false,

                    @SerializedName("is_show_info")
                    @Expose
                    val isShowInfo: Boolean = false,

                    @SerializedName("info_text")
                    @Expose
                    val infoText: String = "")

            data class PenaltyRejectInfo(
                    @SerializedName("is_penalty_reject")
                    @Expose
                    val isPenaltyReject: Boolean = false,

                    @SerializedName("penalty_reject_wording")
                    @Expose
                    val penaltyRejectWording: String = ""
            )
        }
    }
}