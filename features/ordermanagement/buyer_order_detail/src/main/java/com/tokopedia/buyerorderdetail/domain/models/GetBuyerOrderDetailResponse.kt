package com.tokopedia.buyerorderdetail.domain.models

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailLogisticSectionInfoID

data class GetBuyerOrderDetailResponse(
    @Expose
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(
        @Expose
        @SerializedName("mp_bom_detail")
        val buyerOrderDetail: BuyerOrderDetail = BuyerOrderDetail()
    ) {
        data class BuyerOrderDetail(
            @Expose
            @SerializedName("button")
            val button: Button = Button(),
            @Expose
            @SerializedName("cashback_info")
            val cashbackInfo: String = "",
            @Expose
            @SerializedName("preorder")
            val preOrder: PreOrder = PreOrder(),
            @Expose
            @SerializedName("deadline")
            val deadline: Deadline = Deadline(),
            @Expose
            @SerializedName("dot_menus")
            val dotMenu: List<DotMenu> = listOf(),
            @Expose
            @SerializedName("invoice")
            val invoice: String = "",
            @Expose
            @SerializedName("invoice_url")
            val invoiceUrl: String = "",
            @Expose
            @SerializedName("meta")
            val meta: Meta = Meta(),
            @Expose
            @SerializedName("order_id")
            val orderId: String = "0",
            @Expose
            @SerializedName("order_status")
            val orderStatus: OrderStatus = OrderStatus(),
            @Expose
            @SerializedName("payment")
            val payment: Payment = Payment(),
            @Expose
            @SerializedName("payment_date")
            val paymentDate: String = "",
            @Expose
            @SerializedName("shipment")
            val shipment: Shipment = Shipment(),
            @Expose
            @SerializedName("shop")
            val shop: Shop = Shop(),
            @Expose
            @SerializedName("ticker_info")
            val tickerInfo: TickerInfo = TickerInfo(),
            @Expose
            @SerializedName("dropship")
            val dropship: Dropship = Dropship(),
            @Expose
            @SerializedName("ads_page_name")
            val adsPageName: String = "",
            @Expose
            @SerializedName("logistic_section_info")
            val logisticSections: List<LogisticSectionInfo> = listOf(),
            @SerializedName("addon_info")
            @Expose
            val addonInfo: AddonInfo? = AddonInfo(),
            @SerializedName("details")
            @Expose
            val details: Details? = Details(),
            @SerializedName("has_reso_status")
            @Expose
            val hasResoStatus: Boolean? = false,
            @SerializedName("has_ppp")
            @Expose
            val hasInsurance: Boolean? = false,
            @SerializedName("is_pof")
            @Expose
            val isPof: Boolean? = false
        ) {
            fun getDriverTippingInfo(): LogisticSectionInfo? {
                return logisticSections.find { it.id == BuyerOrderDetailLogisticSectionInfoID.DRIVER_TIPPING_INFO }
            }
            fun getPodInfo(): LogisticSectionInfo? {
                return logisticSections.find { it.id == BuyerOrderDetailLogisticSectionInfoID.POD_INFO }
            }

            data class Button(
                @Expose
                @SerializedName("display_name")
                val displayName: String = "",
                @Expose
                @SerializedName("key")
                val key: String = "",
                @Expose
                @SerializedName("popup")
                val popup: Popup = Popup(),
                @Expose
                @SerializedName("variant")
                val variant: String = "",
                @Expose
                @SerializedName("type")
                val type: String = "",
                @Expose
                @SerializedName("url")
                val url: String = ""
            ) {
                data class Popup(
                    @Expose
                    @SerializedName("action_button")
                    val actionButton: List<PopUpButton> = listOf(),
                    @Expose
                    @SerializedName("body")
                    val body: String = "",
                    @Expose
                    @SerializedName("title")
                    val title: String = ""
                ) {
                    data class PopUpButton(
                        @Expose
                        @SerializedName("key")
                        val key: String = "",
                        @Expose
                        @SerializedName("display_name")
                        val displayName: String = "",
                        @Expose
                        @SerializedName("color")
                        val color: String = "",
                        @Expose
                        @SerializedName("type")
                        val type: String = "",
                        @Expose
                        @SerializedName("uri_type")
                        val uriType: String = "",
                        @Expose
                        @SerializedName("uri")
                        val uri: String = ""
                    )
                }
            }

            data class PreOrder(
                @Expose
                @SerializedName("is_preorder")
                val isPreOrder: Boolean = false,
                @Expose
                @SerializedName("label")
                val label: String = "",
                @Expose
                @SerializedName("value")
                val value: String = ""
            )

            data class Deadline(
                @Expose
                @SerializedName("color")
                val color: String = "",
                @Expose
                @SerializedName("label")
                val label: String = "",
                @Expose
                @SerializedName("value")
                val value: String = ""
            )

            data class DotMenu(
                @Expose
                @SerializedName("display_name")
                val displayName: String = "",
                @Expose
                @SerializedName("key")
                val key: String = "",
                @Expose
                @SerializedName("popup")
                val popup: Button.Popup = Button.Popup(),
                @Expose
                @SerializedName("url")
                val url: String = ""
            )

            data class OrderStatus(
                @Expose
                @SerializedName("id")
                val id: String = "0",
                @Expose
                @SerializedName("indicator_color")
                val indicatorColor: String = "",
                @Expose
                @SerializedName("status_name")
                val statusName: String = "",
                @Expose
                @SerializedName("labels")
                val labels: List<Label> = listOf()
            ) {
                data class Label(
                    @Expose
                    @SerializedName("label")
                    val label: String = ""
                )
            }

            data class Payment(
                @Expose
                @SerializedName("payment_amount")
                val paymentAmount: PaymentAmount = PaymentAmount(),
                @Expose
                @SerializedName("payment_details")
                val paymentDetails: List<PaymentDetail> = listOf(),
                @Expose
                @SerializedName("payment_method")
                val paymentMethod: PaymentMethod = PaymentMethod(),
                @SerializedName("payment_refund")
                val paymentRefund: PaymentRefund = PaymentRefund()
            ) {
                data class PaymentAmount(
                    @Expose
                    @SerializedName("label")
                    val label: String = "",
                    @Expose
                    @SerializedName("value")
                    val value: String = ""
                )

                data class PaymentDetail(
                    @Expose
                    @SerializedName("label")
                    val label: String = "",
                    @Expose
                    @SerializedName("value")
                    val value: String = ""
                )

                data class PaymentMethod(
                    @Expose
                    @SerializedName("label")
                    val label: String = "",
                    @Expose
                    @SerializedName("value")
                    val value: String = ""
                )

                data class PaymentRefund(
                    @SerializedName("summary_info")
                    val summaryInfo: SummaryInfo? = SummaryInfo(),
                    @SerializedName("estimate_info")
                    val estimateInfo: EstimateInfo? = EstimateInfo(),
                    @SerializedName("total_amount")
                    val totalAmount: TotalAmount = TotalAmount(),
                    @SerializedName("is_refunded")
                    val isRefunded: Boolean = false
                ) {
                    data class TotalAmount(
                        @SerializedName("label")
                        val label: String = "",
                        @SerializedName("value")
                        val value: String = ""
                    )
                    data class SummaryInfo(
                        @SerializedName("details")
                        val details: List<Detail> = listOf(),
                        @SerializedName("footer")
                        val footer: String = "",
                        @SerializedName("total_amount")
                        val totalAmount: TotalAmount = TotalAmount()
                    ) {
                        data class Detail(
                            @SerializedName("label")
                            val label: String = "",
                            @SerializedName("value")
                            val value: String = ""
                        )
                    }

                    data class EstimateInfo(
                        @SerializedName("title")
                        val title: String = "",
                        @SerializedName("info")
                        val info: String = ""
                    )
                }
            }

            data class Shipment(
                @Expose
                @SerializedName("driver")
                val driver: Driver = Driver(),
                @Expose
                @SerializedName("eta")
                val eta: String = "",
                @Expose
                @SerializedName("receiver")
                val receiver: Receiver = Receiver(),
                @Expose
                @SerializedName("shipping_display_name")
                val shippingDisplayName: String = "",
                @Expose
                @SerializedName("shipping_info")
                val shippingInfo: TickerInfo = TickerInfo(),
                @Expose
                @SerializedName("shipping_ref_num")
                val shippingRefNum: String = "",
                @Expose
                @SerializedName("eta_is_updated")
                val etaIsUpdated: Boolean = false,
                @Expose
                @SerializedName("user_updated_info")
                val userUpdatedInfo: String = ""
            ) {
                data class Driver(
                    @Expose
                    @SerializedName("license_number")
                    val licenseNumber: String = "",
                    @Expose
                    @SerializedName("name")
                    val name: String = "",
                    @Expose
                    @SerializedName("phone")
                    val phone: String = "",
                    @Expose
                    @SerializedName("photo_url")
                    val photoUrl: String = ""
                )

                data class Receiver(
                    @Expose
                    @SerializedName("city")
                    val city: String = "",
                    @Expose
                    @SerializedName("district")
                    val district: String = "",
                    @Expose
                    @SerializedName("name")
                    val name: String = "",
                    @Expose
                    @SerializedName("phone")
                    val phone: String = "",
                    @Expose
                    @SerializedName("postal")
                    val postal: String = "",
                    @Expose
                    @SerializedName("province")
                    val province: String = "",
                    @Expose
                    @SerializedName("street")
                    val street: String = ""
                )
            }

            data class Shop(
                @Expose
                @SerializedName("shop_id")
                val shopId: String = "0",
                @Expose
                @SerializedName("shop_name")
                val shopName: String = "",
                @Expose
                @SerializedName("shop_type")
                val shopType: Int = 0,
                @Expose
                @SerializedName("badge_url")
                val badgeUrl: String = ""
            )

            data class TickerInfo(
                @Expose
                @SerializedName("action_key")
                val actionKey: String = "",
                @Expose
                @SerializedName("action_text")
                val actionText: String = "",
                @Expose
                @SerializedName("action_url")
                val actionUrl: String = "",
                @Expose
                @SerializedName("text")
                val text: String = "",
                @Expose
                @SerializedName("type")
                val type: String = ""
            )

            data class Meta(
                @Expose
                @SerializedName("is_bo")
                val isBebasOngkir: Boolean = false,
                @Expose
                @SerializedName("bo_image_url")
                val boImageUrl: String = "false"
            )

            data class Dropship(
                @Expose
                @SerializedName("name")
                val name: String = "",
                @Expose
                @SerializedName("phone_number")
                val phoneNumber: String = ""
            )

            data class LogisticSectionInfo(
                @Expose
                @SerializedName("index")
                val index: Long = 0L,
                @Expose
                @SerializedName("id")
                val id: String = "",
                @Expose
                @SerializedName("image_link")
                val imageUrl: String = "",
                @Expose
                @SerializedName("title")
                val title: String = "",
                @Expose
                @SerializedName("subtitle")
                val subtitle: String = "",
                @Expose
                @SerializedName("action")
                val action: Action = Action()
            ) {
                data class Action(
                    @Expose
                    @SerializedName("name")
                    val name: String = "",
                    @Expose
                    @SerializedName("link")
                    val link: String = ""
                )
            }

            data class Details(
                @SerializedName("bundle_icon")
                @Expose
                val bundleIcon: String = "",
                @SerializedName("addon_label")
                @Expose
                val addonLabel: String = "",
                @SerializedName("addon_icon")
                @Expose
                val addonIcon: String = "",
                @SerializedName("bundles")
                @Expose
                val bundles: List<Bundle>? = listOf(),
                @SerializedName("non_bundles")
                @Expose
                val nonBundles: List<NonBundle>? = listOf(),
                @SerializedName("partial_fulfillment")
                val partialFulfillment: PartialFulfillment = PartialFulfillment(),
                @SerializedName("ticker_info")
                val tickerInfo: TickerInfo = TickerInfo(),
                @SerializedName("total_products")
                @Expose
                val totalProducts: Long = 0
            ) {
                data class Bundle(
                    @SerializedName("bundle_id")
                    @Expose
                    val bundleId: String = "0",
                    @SerializedName("bundle_name")
                    @Expose
                    val bundleName: String = "",
                    @SerializedName("bundle_price")
                    @Expose
                    val bundlePrice: Double = 0.0,
                    @SerializedName("bundle_quantity")
                    @Expose
                    val bundleQuantity: Int = 0,
                    @SerializedName("bundle_subtotal_price")
                    @Expose
                    val bundleSubtotalPrice: Double = 0.0,
                    @SerializedName("order_detail")
                    @Expose
                    val orderDetail: List<OrderDetail> = listOf()
                ) {
                    data class OrderDetail(
                        @SerializedName("button")
                        @Expose
                        val button: Button? = null,

                        @SerializedName("category_id")
                        @Expose
                        val categoryId: String = "0",

                        @SerializedName("category")
                        @Expose
                        val category: String = "",

                        @Expose
                        @SerializedName("total_price")
                        val totalPrice: String = "0",

                        @Expose
                        @SerializedName("total_price_text")
                        val totalPriceText: String = "",

                        @SerializedName("notes")
                        @Expose
                        val notes: String = "",

                        @SerializedName("order_detail_id")
                        @Expose
                        val orderDetailId: String = "0",

                        @SerializedName("product_desc")
                        @Expose
                        val productDesc: String = "",

                        @SerializedName("product_id")
                        @Expose
                        val productId: String = "0",

                        @SerializedName("product_name")
                        @Expose
                        val productName: String = "",

                        @SerializedName("quantity")
                        @Expose
                        val quantity: Int = 0,

                        @Expose
                        @SuppressLint("Invalid Data Type")
                        @SerializedName("price")
                        val price: Double = 0.0,

                        @Expose
                        @SerializedName("thumbnail")
                        val thumbnail: String = "",

                        @Expose
                        @SerializedName("price_text")
                        val priceText: String = ""
                    )
                }

                data class NonBundle(
                    @SerializedName("addon_summary")
                    @Expose
                    val addonSummary: AddonSummary? = AddonSummary(),

                    @SerializedName("button")
                    @Expose
                    val button: Button? = null,

                    @SerializedName("category_id")
                    @Expose
                    val categoryId: String = "0",

                    @SerializedName("category")
                    @Expose
                    val category: String = "",

                    @Expose
                    @SerializedName("total_price")
                    val totalPrice: String = "0",

                    @Expose
                    @SerializedName("total_price_text")
                    val totalPriceText: String = "",

                    @SerializedName("notes")
                    @Expose
                    val notes: String = "",

                    @SerializedName("order_detail_id")
                    @Expose
                    val orderDetailId: String = "0",

                    @SerializedName("product_desc")
                    @Expose
                    val productDesc: String = "",

                    @SerializedName("product_id")
                    @Expose
                    val productId: String = "0",

                    @SerializedName("product_name")
                    @Expose
                    val productName: String = "",

                    @SerializedName("quantity")
                    @Expose
                    val quantity: Int = 0,

                    @Expose
                    @SuppressLint("Invalid Data Type")
                    @SerializedName("price")
                    val price: Double = 0.0,

                    @Expose
                    @SerializedName("thumbnail")
                    val thumbnail: String = "",

                    @Expose
                    @SerializedName("price_text")
                    val priceText: String = ""
                ) {

                    data class AddonSummary(
                        @SerializedName("addons")
                        @Expose
                        val addons: List<Addon>? = listOf(),
                        @SerializedName("total")
                        @Expose
                        val total: Double = 0.0,
                        @SerializedName("total_price")
                        @Expose
                        val totalPrice: Double = 0.0,
                        @SerializedName("total_price_str")
                        @Expose
                        val totalPriceStr: String = "",
                        @SerializedName("total_quantity")
                        @Expose
                        val totalQuantity: Int = 0
                    ) {
                        data class Addon(
                            @SerializedName("id")
                            @Expose
                            val id: String = "0",
                            @SerializedName("image_url")
                            @Expose
                            val imageUrl: String = "",
                            @SerializedName("metadata")
                            @Expose
                            val metadata: AddonInfo.OrderLevel.Addon.Metadata? = AddonInfo.OrderLevel.Addon.Metadata(),
                            @SerializedName("name")
                            @Expose
                            val name: String = "",
                            @SerializedName("order_id")
                            @Expose
                            val orderId: String = "0",
                            @SerializedName("price_str")
                            @Expose
                            val priceStr: String = "",
                            @SerializedName("quantity")
                            @Expose
                            val quantity: Int = 0,
                            @SerializedName("subtotal_price")
                            @Expose
                            val subtotalPrice: Double = 0.0,
                            @SerializedName("subtotal_price_str")
                            @Expose
                            val subtotalPriceStr: String = "",
                            @SerializedName("type")
                            @Expose
                            val type: String = ""
                        )
                    }
                }

                data class PartialFulfillment(
                    @SerializedName("fulfilled")
                    val fulfilled: Fulfilled = Fulfilled(),
                    @SerializedName("unfulfilled")
                    val unfulfilled: Unfulfilled = Unfulfilled()
                ) {
                    data class Fulfilled(
                        @SerializedName("header")
                        val header: Header = Header()
                    )

                    data class Unfulfilled(
                        @SerializedName("details")
                        val details: List<NonBundle> = listOf(),
                        @SerializedName("header")
                        val header: Header = Header()
                    )

                    data class Header(
                        @SerializedName("quantity")
                        val quantity: String = "",
                        @SerializedName("title")
                        val title: String = ""
                    )
                }
            }

            data class AddonInfo(
                @SerializedName("icon_url")
                @Expose
                val iconUrl: String = "",
                @SerializedName("label")
                @Expose
                val label: String = "",
                @SerializedName("order_level")
                @Expose
                val orderLevel: OrderLevel? = OrderLevel()
            ) {

                data class OrderLevel(
                    @SerializedName("addons")
                    @Expose
                    val addons: List<Addon>? = listOf(),
                    @SerializedName("total")
                    @Expose
                    val total: Long = 0,
                    @SerializedName("total_price")
                    @Expose
                    val totalPrice: Double = 0.0,
                    @SerializedName("total_price_str")
                    @Expose
                    val totalPriceStr: String = "",
                    @SerializedName("total_quantity")
                    @Expose
                    val totalQuantity: Int = 0
                ) {
                    data class Addon(
                        @SerializedName("id")
                        @Expose
                        val id: String = "0",
                        @SerializedName("image_url")
                        @Expose
                        val imageUrl: String = "",
                        @SerializedName("metadata")
                        @Expose
                        val metadata: Metadata? = Metadata(),
                        @SerializedName("name")
                        @Expose
                        val name: String = "",
                        @SerializedName("order_id")
                        @Expose
                        val orderId: String = "0",
                        @SerializedName("price_str")
                        @Expose
                        val priceStr: String = "",
                        @SerializedName("price")
                        @Expose
                        val price: Double = 0.0,
                        @SerializedName("quantity")
                        @Expose
                        val quantity: Int = 0,
                        @SerializedName("subtotal_price_str")
                        @Expose
                        val subtotalPriceStr: String = "",
                        @SerializedName("type")
                        @Expose
                        val type: String = ""
                    ) {
                        data class Metadata(
                            @SerializedName("add_on_note")
                            @Expose
                            val addonNote: AddonNote = AddonNote()
                        ) {
                            data class AddonNote(
                                @SerializedName("notes")
                                @Expose
                                val notes: String = "",
                                @SerializedName("short_notes")
                                @Expose
                                val shortNotes: String = "",
                                @SerializedName("to")
                                @Expose
                                val to: String = "",
                                @SerializedName("from")
                                @Expose
                                val from: String = ""
                            )
                        }
                    }
                }
            }
        }
    }
}
