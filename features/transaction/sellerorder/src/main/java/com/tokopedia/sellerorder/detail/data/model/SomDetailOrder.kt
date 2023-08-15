package com.tokopedia.sellerorder.detail.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.common.domain.model.TickerInfo
import com.tokopedia.sellerorder.common.presenter.model.PopUp
import kotlinx.parcelize.Parcelize

/**
 * Created by fwidjaja on 2019-08-27.
 */
data class SomDetailOrder(
    @SerializedName("data")
    @Expose
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("get_som_detail")
        @Expose
        val getSomDetail: GetSomDetail = GetSomDetail()
    ) {

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
            val tickerInfo: TickerInfo = TickerInfo(),

            @SerializedName("details")
            @Expose
            val details: Details = Details(),

            @SerializedName("addon_info")
            @Expose
            val addOnInfo: AddOnInfo? = null,

            @SerializedName("has_reso_status")
            val hasResoStatus: Boolean? = false
        ) {

            fun getFirstProduct(): Details.Product? {
                return details.nonBundle?.firstOrNull()
                    ?: details.bundle?.firstOrNull()?.orderDetail?.firstOrNull()
            }

            fun getProductList(): List<Details.Product> {
                return details.nonBundle.orEmpty().plus(details.bundle?.flatMap { it.orderDetail }.orEmpty()).distinctBy {
                    it.id
                }
            }

            data class Bmgm(
                @SerializedName("bmgm_tier_name")
                val bmgmTierName: String = "",
                @SerializedName("id")
                val id: String = "",
                @SerializedName("order_detail")
                val orderDetail: List<OrderDetail> = listOf(),
                @SerializedName("price_after_benefit")
                val priceAfterBenefit: Double = 0.0,
                @SerializedName("price_after_benefit_formatted")
                val priceAfterBenefitFormatted: String = "",
                @SerializedName("price_before_benefit")
                val priceBeforeBenefit: Double = 0.0,
                @SerializedName("price_before_benefit_formatted")
                val priceBeforeBenefitFormatted: String = "",
                @SerializedName("tier_discount_amount")
                val tierDiscountAmount: Int = 0,
                @SerializedName("tier_discount_amount_formatted")
                val tierDiscountAmountFormatted: String = ""
            ) {
                data class OrderDetail(
                    @SerializedName("id")
                    val id: String = "0",
                    @SerializedName("order_detail_id")
                    val orderDtlId: String = "0",
                    @SerializedName("name")
                    val productName: String = "",
                    @SerializedName("thumbnail")
                    val thumbnail: String = "",
                    @SerializedName("price")
                    val price: Double = 0.0,
                    @SerializedName("price_text")
                    val priceText: String = "",
                    @SerializedName("quantity")
                    val quantity: Int = 0,
                    @SerializedName("note")
                    val note: String = "",
                    @SerializedName("addon_summary")
                    val addonSummary: AddOnSummary = AddOnSummary()
                )
            }

            data class AddOnInfo(
                @SerializedName("order_level")
                @Expose
                val orderLevelAddOnSummary: AddOnSummary? = null,
                @SerializedName("icon_url")
                @Expose
                val iconUrl: String = "",
                @SerializedName("label")
                @Expose
                val label: String = ""
            )

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
                val phone: String = ""
            )

            data class Details(
                @SerializedName("bundles")
                @Expose
                val bundle: List<Bundle>? = null,
                @SerializedName("non_bundles")
                @Expose
                val nonBundle: List<Product>? = null,
                @SerializedName("bundle_icon")
                @Expose
                val bundleIcon: String = "",
                @SerializedName("bmgm_icon")
                val bmgmIcon: String = "",
                @SerializedName("bmgms")
                val bmgms: List<Bmgm>? = null,
                @SerializedName("addon_icon")
                @Expose
                val addOnIcon: String = "",
                @SerializedName("addon_label")
                @Expose
                val addOnLabel: String = ""
            ) {
                data class Bundle(
                    @SerializedName("bundle_id")
                    @Expose
                    val bundleId: String = "",
                    @SerializedName("bundle_name")
                    @Expose
                    val bundleName: String = "",
                    @SerializedName("bundle_price")
                    @Expose
                    val bundlePrice: String = "0",
                    @SerializedName("bundle_subtotal_price")
                    @Expose
                    val bundleSubtotalPrice: String = "0",
                    @SerializedName("order_detail")
                    @Expose
                    val orderDetail: List<Product> = listOf()
                )

                data class Product(
                    @SerializedName("id")
                    @Expose
                    val id: String = "",
                    @SerializedName("name")
                    @Expose
                    val name: String = "",
                    @SerializedName("note")
                    @Expose
                    val note: String = "",
                    @SerializedName("order_detail_id")
                    @Expose
                    val orderDetailId: String = "",
                    @SerializedName("price_text")
                    @Expose
                    val priceText: String = "",
                    @SerializedName("quantity")
                    @Expose
                    val quantity: Int = 0,
                    @SerializedName("thumbnail")
                    @Expose
                    val thumbnail: String = "",
                    @SerializedName("addon_summary")
                    @Expose
                    val addOnSummary: AddOnSummary? = null
                )
            }

            data class Dropshipper(
                @SerializedName("phone")
                @Expose
                val phone: String = "",

                @SerializedName("name")
                @Expose
                val name: String = ""
            )

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

                @SerializedName("courier_info")
                val courierInfo: String = "",

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
                val awbUploadProofText: String = ""
            )

            data class BookingInfo(
                @SerializedName("driver")
                @Expose
                val driver: Driver = Driver(),

                @SerializedName("online_booking")
                @Expose
                val onlineBooking: OnlineBooking = OnlineBooking()
            ) {

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
                val province: String = ""
            )

            data class Deadline(
                @SerializedName("text")
                @Expose
                val text: String = "",

                @SerializedName("color")
                @Expose
                val color: String = "",

                @SerializedName("style")
                val style: Int = Int.ZERO
            )

            data class Insurance(
                @SerializedName("type")
                @Expose
                val text: Int = 0,

                @SerializedName("name")
                @Expose
                val name: String = "",

                @SerializedName("note")
                @Expose
                val note: String = ""
            )

            data class Warehouse(
                @SerializedName("warehouse_id")
                @Expose
                val warehouseId: String = "0",

                @SerializedName("fulfill_by")
                @Expose
                val fullFillBy: Int = 0
            )

            data class LabelInfo(
                @SerializedName("flag_name")
                @Expose
                val flagName: String = "",

                @SerializedName("flag_color")
                @Expose
                val flagColor: String = "",

                @SerializedName("flag_background")
                @Expose
                val flagBg: String = ""
            )

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
                val status: Int = 0
            )

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
                val isShippingPrinted: Boolean = false,

                @SerializedName("is_broadcast_chat")
                @Expose
                val isBroadcastChat: Boolean = false,

                @SerializedName("shipment_logo")
                @Expose
                val shipmentLogo: String = ""
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
                ) : Parcelable

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
                val popUp: PopUp = PopUp()
            )

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
                val infoText: String = ""
            )

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
