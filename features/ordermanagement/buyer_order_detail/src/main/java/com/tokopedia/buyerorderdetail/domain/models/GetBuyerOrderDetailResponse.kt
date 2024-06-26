package com.tokopedia.buyerorderdetail.domain.models

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailLogisticSectionInfoID
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.order_management_common.domain.data.AddOnSummary
import com.tokopedia.order_management_common.domain.data.ProductBenefit

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
            @SerializedName("group_type")
            val groupType: String = "0",
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
            @SerializedName("additional_data")
            @Expose
            val additionalData: BomAdditionalData? = null,
            @SerializedName("is_pof")
            @Expose
            val isPof: Boolean? = false,
            @SerializedName("is_plus")
            @Expose
            val isPlus: Boolean? = false,
            @SerializedName("widget")
            @Expose
            val widget: Widget? = null
        ) {
            fun getDriverTippingInfo(): LogisticSectionInfo? {
                return logisticSections.find { it.id == BuyerOrderDetailLogisticSectionInfoID.DRIVER_TIPPING_INFO }
            }

            fun getPodInfo(): LogisticSectionInfo? {
                return logisticSections.find { it.id == BuyerOrderDetailLogisticSectionInfoID.POD_INFO }
            }

            data class BomAdditionalData(
                @SerializedName("epharmacy_data")
                @Expose
                val epharmacyData: EpharmacyData? = null,
                @SerializedName("plus_savings")
                @Expose
                val plusSavings: PlusSavings? = null,
                @SerializedName("group_order_data")
                val groupOrderData: GroupOrderData? = null
            ) {
                data class GroupOrderData(
                    @SerializedName("tx_id")
                    val txId: String = "0",
                    @SerializedName("icon_url")
                    val iconUrl: String = "",
                    @SerializedName("title")
                    val title: String = "",
                    @SerializedName("description")
                    val description: String = ""
                )

                data class EpharmacyData(
                    @SerializedName("consultation_date")
                    @Expose
                    val consultationDate: String = String.EMPTY,
                    @SerializedName("consultation_doctor_name")
                    @Expose
                    val consultationDoctorName: String = String.EMPTY,
                    @SerializedName("consultation_expiry_date")
                    @Expose
                    val consultationExpiryDate: String = String.EMPTY,
                    @SerializedName("consultation_name")
                    @Expose
                    val consultationName: String = String.EMPTY,
                    @SerializedName("consultation_patient_name")
                    @Expose
                    val consultationPatientName: String = String.EMPTY,
                    @SerializedName("consultation_prescription_number")
                    @Expose
                    val consultationPrescriptionNumber: String = String.EMPTY
                )
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
                val paymentRefund: PaymentRefund? = PaymentRefund()
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
                @SerializedName("title")
                val title: String = String.EMPTY,
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
                val userUpdatedInfo: String = "",
                @SerializedName("buttons")
                val buttons: List<Button> = listOf()
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

                data class Button(
                    @SerializedName("key")
                    val key: String = "",
                    @SerializedName("icon")
                    val icon: String = "",
                    @SerializedName("action_type")
                    val actionType: String = "",
                    @SerializedName("value")
                    val value: String = ""
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

            data class AddonInfo(
                @SerializedName("icon_url")
                @Expose
                val iconUrl: String = "",
                @SerializedName("label")
                @Expose
                val label: String = "",
                @SerializedName("order_level")
                @Expose
                val addonSummary: AddOnSummary? = null
            )

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
                val partialFulfillment: PartialFulfillment? = PartialFulfillment(),
                @SerializedName("bmgm_icon")
                val bmgmIcon: String = "",
                @SerializedName("bmgms")
                val bmgms: List<Bmgm>? = null,
                @SerializedName("ticker_info")
                val tickerInfo: TickerInfo = TickerInfo(),
                @SerializedName("total_products")
                @Expose
                val totalProducts: Long = 0
            ) {

                data class Bmgm(
                    @SerializedName("bmgm_tier_name")
                    val bmgmTierName: String = "",
                    @SerializedName("total_price_note")
                    val totalPriceNote: String = "",
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
                    val tierDiscountAmountFormatted: String = "",
                    @SerializedName("product_benefit")
                    val productBenefit: ProductBenefit? = null
                ) {
                    data class OrderDetail(
                        @SerializedName("order_detail_id")
                        val orderDetailId: String = "0",
                        @SerializedName("product_id")
                        val productId: String = "0",
                        @SerializedName("product_name")
                        val productName: String = "",
                        @SerializedName("product_url")
                        val productUrl: String = "",
                        @SerializedName("thumbnail")
                        val thumbnail: String = "",
                        @SerializedName("price")
                        val price: Double = 0.0,
                        @SerializedName("price_text")
                        val priceText: String = "",
                        @SerializedName("quantity")
                        val quantity: Int = 0,
                        @SerializedName("total_price")
                        val totalPrice: Double = 0.0,
                        @SerializedName("total_price_text")
                        val totalPriceText: String = "",
                        @SerializedName("notes")
                        val notes: String = "",
                        @SerializedName("button")
                        val button: Button? = Button(),
                        @SerializedName("category")
                        val category: String = "0",
                        @SerializedName("category_id")
                        val categoryId: String = "0",
                        @SerializedName("addon_summary")
                        val addonSummary: AddOnSummary? = null
                    )
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

                        @SerializedName("product_url")
                        @Expose
                        val productUrl: String = "",

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
                        val priceText: String = "",

                        @SerializedName("addon_summary")
                        @Expose
                        val addonSummary: AddOnSummary? = null
                    )
                }

                data class NonBundle(
                    @SerializedName("addon_summary")
                    @Expose
                    val addonSummary: AddOnSummary? = null,

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
                    val priceText: String = "",

                    @Expose
                    @SerializedName("product_url")
                    val productUrl: String = ""
                )
            }

            data class Widget(
                @SerializedName("reso_status")
                @Expose
                val resoStatus: ResoStatus? = null,
                @SerializedName("reso_csat")
                @Expose
                val resoCsat: ResoCsat? = null,
                @SerializedName("ppp")
                @Expose
                val ppp: Ppp? = null
            ) {
                data class ResoStatus(
                    @SerializedName("show")
                    @Expose
                    val show: Boolean = false
                )
                data class ResoCsat(
                    @SerializedName("show")
                    @Expose
                    val show: Boolean = false,
                    @SerializedName("help_url")
                    @Expose
                    val helpUrl: String = ""
                )
                data class Ppp(
                    @SerializedName("show")
                    @Expose
                    val show: Boolean = false
                )
            }
        }
    }
}
