package com.tokopedia.sellerorder.waitingpaymentorder.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

data class WaitingPaymentOrderResponse(
        @SerializedName("data")
        @Expose
        val data: Data = Data()
) {
    data class Data(
            @SerializedName("orderListWaitingPayment")
            @Expose
            val waitingPaymentOrder: WaitingPaymentOrder = WaitingPaymentOrder()
    ) {
        data class WaitingPaymentOrder(
                @SerializedName("total_data_per_batch")
                @Expose
                val totalDataPerBatch: Int = 1,

                @SerializedName("cursor_payment_deadline")
                @Expose
                val cursorPaymentDeadline: Long = 0L,

                @SerializedName("list")
                @Expose
                val orders: List<Order> = listOf()
        ) {

            data class Order(
                    @SerializedName("order_id")
                    @Expose
                    val orderId: String = "",

                    @SerializedName("payment_deadline")
                    @Expose
                    val paymentDeadline: String = "",

                    @SerializedName("buyer_name")
                    @Expose
                    val buyerNameAndPlace: String = "",

                    @SerializedName("have_product_bundle")
                    @Expose
                    val haveProductBundle: Boolean = false,

                    @SerializedName("products")
                    @Expose
                    val products: List<Product> = listOf(),

                    @SerializedName("bundle_detail")
                    @Expose
                    val bundleDetail: BundleDetail? = null
            ) {
                data class Product(
                        @SerializedName("product_id")
                        @Expose
                        val id: String = "",

                        @SerializedName("product_name")
                        @Expose
                        val name: String = "",

                        @SerializedName("product_picture")
                        @Expose
                        val picture: String = "",

                        @SerializedName("product_qty")
                        @Expose
                        val quantity: Int = 0,

                        @SerializedName("product_price")
                        @Expose
                        val price: String = ""
                )

                data class BundleDetail(
                    @SerializedName("product_bundling_icon")
                    @Expose
                    val productBundlingIcon: String = "",

                    @SerializedName("bundle")
                    @Expose
                    val bundle: List<Bundle> = listOf(),

                    @SerializedName("non_bundle")
                    @Expose
                    val nonBundle: List<Product> = listOf()
                ) {
                    data class Bundle(
                        @SerializedName("bundle_name")
                        @Expose
                        val name: String = "",

                        @SerializedName("order_detail")
                        @Expose
                        val products: List<Product> = listOf()
                    )
                }
            }
        }
    }
}