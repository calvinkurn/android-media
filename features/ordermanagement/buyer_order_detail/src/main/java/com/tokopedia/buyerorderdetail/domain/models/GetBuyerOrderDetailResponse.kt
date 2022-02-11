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
            @SerializedName("products")
            val products: List<Product> = listOf(),
            @Expose
            @SerializedName("have_product_bundle")
            val haveProductBundle: Boolean = false,
            @Expose
            @SerializedName("bundle_detail")
            val bundleDetail: BundleDetail? = BundleDetail(),
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
            val details: Details? = Details()
        ) {
            fun getDriverTippingInfo(): LogisticSectionInfo? {
                return logisticSections.find { it.id == BuyerOrderDetailLogisticSectionInfoID.DRIVER_TIPPING_INFO }
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
                val statusName: String = ""
            )

            data class Payment(
                @Expose
                @SerializedName("payment_amount")
                val paymentAmount: PaymentAmount = PaymentAmount(),
                @Expose
                @SerializedName("payment_details")
                val paymentDetails: List<PaymentDetail> = listOf(),
                @Expose
                @SerializedName("payment_method")
                val paymentMethod: PaymentMethod = PaymentMethod()
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
            }

            data class Product(
                @Expose
                @SerializedName("button")
                val button: Button = Button(),
                @Expose
                @SerializedName("category")
                val category: String = "",
                @Expose
                @SerializedName("category_id")
                val categoryId: String = "0",
                @Expose
                @SerializedName("notes")
                val notes: String = "",
                @Expose
                @SerializedName("bundle_id")
                val bundleId: String? = "0",
                @Expose
                @SerializedName("order_detail_id")
                val orderDetailId: String = "0",
                @Expose
                @SuppressLint("Invalid Data Type")
                @SerializedName("price")
                val price: Double = 0.0,
                @Expose
                @SerializedName("price_text")
                val priceText: String = "",
                @Expose
                @SerializedName("product_id")
                val productId: String = "0",
                @Expose
                @SerializedName("product_name")
                val productName: String = "",
                @Expose
                @SerializedName("product_url")
                val productUrl: String = "",
                @Expose
                @SerializedName("quantity")
                val quantity: Int = 0,
                @Expose
                @SerializedName("thumbnail")
                val thumbnail: String = "",
                @Expose
                @SerializedName("total_price")
                val totalPrice: String = "0",
                @Expose
                @SerializedName("total_price_text")
                val totalPriceText: String = "",
            )

            data class BundleDetail(
                @Expose
                @SerializedName("bundle")
                val bundleList: List<Bundle> = listOf(),
                @Expose
                @SerializedName("product_bundling_icon")
                val bundleIcon: String? = "",
                @Expose
                @SerializedName("non_bundle")
                val nonBundleList: List<Product> = listOf()
            ) {
                data class Bundle(
                    @Expose
                    @SerializedName("bundle_id")
                    val bundleId: String = "0",
                    @Expose
                    @SerializedName("bundle_name")
                    val bundleName: String = "",
                    @Expose
                    @SerializedName("bundle_price")
                    val bundlePrice: Double = 0.0,
                    @SerializedName("bundle_quantity")
                    val bundleQty: Int = 0,
                    @Expose
                    @SerializedName("bundle_subtotal_price")
                    val bundleSubtotalPrice: Double = 0.0,
                    @Expose
                    @SerializedName("order_detail")
                    val orderDetailList: List<Product> = listOf()
                )
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
                val index: Int,
                @Expose
                @SerializedName("id")
                val id: String,
                @Expose
                @SerializedName("image_link")
                val imageUrl: String,
                @Expose
                @SerializedName("title")
                val title: String,
                @Expose
                @SerializedName("subtitle")
                val subtitle: String,
                @Expose
                @SerializedName("action")
                val action: Action
            ) {
                data class Action(
                    @Expose
                    @SerializedName("name")
                    val name: String,
                    @Expose
                    @SerializedName("link")
                    val link: String
                )
            }
        }
    }
}