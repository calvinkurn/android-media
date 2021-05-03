package com.tokopedia.buyerorderdetail.domain.models


import com.google.gson.annotations.SerializedName

data class GetBuyerOrderDetailResponse(
        @SerializedName("data")
        val data: Data = Data()
) {
    data class Data(
            @SerializedName("mpBOMDetail")
            val buyerOrderDetail: BuyerOrderDetail = BuyerOrderDetail()
    ) {
        data class BuyerOrderDetail(
                @SerializedName("button")
                val button: Button = Button(),
                @SerializedName("cashback_info")
                val cashbackInfo: String = "",
                @SerializedName("deadline")
                val deadline: Deadline = Deadline(),
                @SerializedName("dot_menu")
                val dotMenu: List<DotMenu> = listOf(),
                @SerializedName("invoice")
                val invoice: String = "",
                @SerializedName("invoice_url")
                val invoiceUrl: String = "",
                @SerializedName("meta")
                val meta: Meta = Meta(),
                @SerializedName("order_id")
                val orderId: String = "0",
                @SerializedName("order_status")
                val orderStatus: OrderStatus = OrderStatus(),
                @SerializedName("payment")
                val payment: Payment = Payment(),
                @SerializedName("payment_date")
                val paymentDate: String = "",
                @SerializedName("products")
                val products: List<Product> = listOf(),
                @SerializedName("shipment")
                val shipment: Shipment = Shipment(),
                @SerializedName("shop")
                val shop: Shop = Shop(),
                @SerializedName("ticker_info")
                val tickerInfo: TickerInfo = TickerInfo()
        ) {
            data class Button(
                    @SerializedName("display_name")
                    val displayName: String = "",
                    @SerializedName("key")
                    val key: String = "",
                    @SerializedName("popup")
                    val popup: Popup = Popup(),
                    @SerializedName("style")
                    val style: String = "",
                    @SerializedName("url")
                    val url: String = ""
            ) {
                data class Popup(
                        @SerializedName("action_button")
                        val actionButton: List<Button> = listOf(),
                        @SerializedName("body")
                        val body: String = "",
                        @SerializedName("title")
                        val title: String = ""
                )
            }

            data class Deadline(
                    @SerializedName("color")
                    val color: String = "",
                    @SerializedName("label")
                    val label: String = "",
                    @SerializedName("value")
                    val value: String = ""
            )

            data class DotMenu(
                    @SerializedName("display_name")
                    val displayName: String = "",
                    @SerializedName("key")
                    val key: String = "",
                    @SerializedName("popup")
                    val popup: Button.Popup = Button.Popup(),
                    @SerializedName("url")
                    val url: String = ""
            )

            data class Meta(
                    @SerializedName("is_os")
                    val isOs: Boolean = false,
                    @SerializedName("is_pm")
                    val isPm: Boolean = false
            )

            data class OrderStatus(
                    @SerializedName("id")
                    val id: String = "0",
                    @SerializedName("indicator_color")
                    val indicatorColor: String = "#FFC400",
                    @SerializedName("status_name")
                    val statusName: String = ""
            )

            data class Payment(
                    @SerializedName("payment_amount")
                    val paymentAmount: PaymentAmount = PaymentAmount(),
                    @SerializedName("payment_details")
                    val paymentDetails: List<PaymentDetail> = listOf(),
                    @SerializedName("payment_method")
                    val paymentMethod: PaymentMethod = PaymentMethod()
            ) {
                data class PaymentAmount(
                        @SerializedName("label")
                        val label: String = "",
                        @SerializedName("value")
                        val value: String = ""
                )

                data class PaymentDetail(
                        @SerializedName("label")
                        val label: String = "",
                        @SerializedName("value")
                        val value: String = ""
                )

                data class PaymentMethod(
                        @SerializedName("label")
                        val label: String = "",
                        @SerializedName("value")
                        val value: String = ""
                )
            }

            data class Product(
                    @SerializedName("button")
                    val button: Button = Button(),
                    @SerializedName("notes")
                    val notes: String = "",
                    @SerializedName("order_detail_id")
                    val orderDetailId: String = "0",
                    @SerializedName("price")
                    val price: String = "0",
                    @SerializedName("price_text")
                    val priceText: String = "",
                    @SerializedName("product_id")
                    val productId: String = "0",
                    @SerializedName("product_name")
                    val productName: String = "",
                    @SerializedName("product_url")
                    val productUrl: String = "",
                    @SerializedName("quantity")
                    val quantity: Int = 0,
                    @SerializedName("thumbnail")
                    val thumbnail: String = "",
                    @SerializedName("total_price")
                    val totalPrice: String = "0",
                    @SerializedName("total_price_text")
                    val totalPriceText: String = ""
            )

            data class Shipment(
                    @SerializedName("driver")
                    val driver: Driver = Driver(),
                    @SerializedName("eta")
                    val eta: String = "",
                    @SerializedName("is_bebas_ongkir")
                    val isBebasOngkir: Boolean = false,
                    @SerializedName("receiver")
                    val receiver: Receiver = Receiver(),
                    @SerializedName("shipping_display_name")
                    val shippingDisplayName: String = "",
                    @SerializedName("shipping_info")
                    val shippingInfo: ShippingInfo = ShippingInfo(),
                    @SerializedName("shipping_ref_num")
                    val shippingRefNum: String = ""
            ) {
                data class Driver(
                        @SerializedName("license_number")
                        val licenseNumber: String = "",
                        @SerializedName("name")
                        val name: String = "",
                        @SerializedName("phone")
                        val phone: String = "",
                        @SerializedName("photo_url")
                        val photoUrl: String = ""
                )

                data class Receiver(
                        @SerializedName("city")
                        val city: String = "",
                        @SerializedName("district")
                        val district: String = "",
                        @SerializedName("name")
                        val name: String = "",
                        @SerializedName("phone")
                        val phone: String = "",
                        @SerializedName("postal")
                        val postal: String = "",
                        @SerializedName("province")
                        val province: String = "",
                        @SerializedName("street")
                        val street: String = ""
                )

                data class ShippingInfo(
                        @SerializedName("notes")
                        val notes: String = "",
                        @SerializedName("title")
                        val title: String = "",
                        @SerializedName("url_detail")
                        val urlDetail: String = "",
                        @SerializedName("url_text")
                        val urlText: String = ""
                )
            }

            data class Shop(
                    @SerializedName("shop_id")
                    val shopId: String = "0",
                    @SerializedName("shop_name")
                    val shopName: String = ""
            )

            data class TickerInfo(
                    @SerializedName("action_key")
                    val actionKey: String = "",
                    @SerializedName("action_text")
                    val actionText: String = "",
                    @SerializedName("action_url")
                    val actionUrl: String = "",
                    @SerializedName("text")
                    val text: String = "",
                    @SerializedName("type")
                    val type: String = ""
            )
        }
    }
}