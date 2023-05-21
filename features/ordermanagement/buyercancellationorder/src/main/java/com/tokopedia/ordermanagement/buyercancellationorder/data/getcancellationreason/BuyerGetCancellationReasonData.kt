package com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel.BuyerCancellationOrderWrapperUiModel

data class BuyerGetCancellationReasonData(

    @SerializedName("data")
    @Expose
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("get_cancellation_reason")
        @Expose
        val getCancellationReason: GetCancellationReason = GetCancellationReason()
    ) {

        data class GetCancellationReason(
            @SerializedName("reasons")
            @Expose
            val reasons: List<ReasonsItem> = emptyList(),

            @SerializedName("is_eligible_instant_cancel")
            @Expose
            val isEligibleInstantCancel: Boolean = false,

            @SerializedName("is_show_ticker")
            @Expose
            val isShowTicker: Boolean = false,

            @SerializedName("ticker_info")
            @Expose
            val tickerInfo: TickerInfo = TickerInfo(),

            @SerializedName("order_details")
            @Expose
            val orderDetails: List<OrderDetailsCancellation> = emptyList(),

            @SerializedName("grouped_orders")
            val groupedOrders: GroupedOrders = GroupedOrders(),

            @SerializedName("group_type")
            val groupType: Int = BuyerCancellationOrderWrapperUiModel.NORMAL_ORDER_TYPE,

            @SerializedName("have_product_bundle")
            @Expose
            val haveProductBundle: Boolean = false,

            @SerializedName("bundle_detail")
            @Expose
            val bundleDetail: BundleDetail? = BundleDetail()
        ) {

            data class GroupedOrders(
                @SerializedName("order_details")
                val orderDetails: List<OrderDetail> = listOf(),
                @SerializedName("ticker")
                val ticker: TickerInfo = TickerInfo(),
                @SerializedName("title")
                val title: String = ""
            ) {
                data class OrderDetail(
                    @SerializedName("invoice_ref_num")
                    val invoiceRefNum: String = "",
                    @SerializedName("product_image")
                    val productImage: String = "",
                    @SerializedName("product_info")
                    val productInfo: String = "",
                    @SerializedName("product_name")
                    val productName: String = "",
                    @SerializedName("product_price")
                    val productPrice: Double = 0.0,
                    @SerializedName("product_qty")
                    val productQty: Int = 0,
                    @SerializedName("shop_icon")
                    val shopIcon: String = "",
                    @SerializedName("shop_name")
                    val shopName: String = ""
                )
            }

            data class TickerInfo(
                @SerializedName("text")
                @Expose
                val text: String = "",

                @SerializedName("type")
                @Expose
                val type: String = "",

                @SerializedName("action_text")
                @Expose
                val actionText: String = "",

                @SerializedName("action_url")
                @Expose
                val actionUrl: String = ""
            )

            data class ReasonsItem(
                @SerializedName("sub_reasons")
                @Expose
                val subReasons: List<SubReasonsItem> = emptyList(),

                @SerializedName("title")
                @Expose
                val title: String = ""
            ) {

                data class SubReasonsItem(
                    @SerializedName("r_code")
                    @Expose
                    val rCode: Int = -1,

                    @SerializedName("reason")
                    @Expose
                    val reason: String = ""
                )
            }

            data class BundleDetail(
                @SerializedName("bundle")
                @Expose
                val bundleList: List<Bundle> = listOf(),

                @SerializedName("product_bundling_icon")
                @Expose
                val bundleIcon: String? = "",

                @SerializedName("non_bundle")
                @Expose
                val nonBundleList: List<OrderDetailsCancellation> = listOf()
            ) {
                data class Bundle(
                    @SerializedName("bundle_name")
                    @Expose
                    val bundleName: String = "",

                    @SerializedName("order_detail")
                    @Expose
                    val orderDetailList: List<OrderDetailsCancellation> = listOf()
                )
            }

            data class OrderDetailsCancellation(
                @SerializedName("product_id")
                @Expose
                val productId: String = "",

                @SerializedName("product_name")
                @Expose
                val productName: String = "",

                @SerializedName("product_price")
                @Expose
                val productPrice: String = "",

                @SerializedName("picture")
                @Expose
                val picture: String = "",

                @SerializedName("bundle_id")
                @Expose
                val bundleId: String = "",

                @SerializedName("bundle_variant_id")
                @Expose
                val bundleVariantId: String = ""
            )
        }
    }
}
