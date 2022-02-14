package com.tokopedia.buyerorderdetail.domain.models

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
    @SerializedName("total_products")
    @Expose
    val totalProducts: Int = 0
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
            val button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button = GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button(),

            @SerializedName("bundle_id")
            @Expose
            val bundleId: String = "0",

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
        val button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button? = GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button(),

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
        val total: Int = 0,
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
                    @Expose
                    @SerializedName("is_custom_note")
                    val isCustomNote: Boolean = false,
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
                    val from: String = "",
                )
            }
        }
    }
}