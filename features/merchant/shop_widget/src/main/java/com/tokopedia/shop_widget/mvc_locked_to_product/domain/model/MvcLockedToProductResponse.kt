package com.tokopedia.shop_widget.mvc_locked_to_product.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toIntOrZero

data class MvcLockedToProductResponse(
    @SerializedName("shopPageGetMVCLockToProduct")
    @Expose
    val shopPageMVCProductLock: ShopPageMVCProductLock = ShopPageMVCProductLock()
) {
    data class ShopPageMVCProductLock(
        @SerializedName("nextPage")
        @Expose
        var nextPage: Int = 0,
        @SerializedName("voucher")
        @Expose
        var voucher: Voucher = Voucher(),
        @SerializedName("productList")
        @Expose
        var productList: ProductList = ProductList(),
        @SerializedName("error")
        @Expose
        var error: Error = Error()
    ) {
        data class Voucher(
            @SerializedName("shopImage")
            @Expose
            var shopImage: String = "",
            @SerializedName("title")
            @Expose
            var title: String = "",
            @SerializedName("baseCode")
            @Expose
            var baseCode: String = "",
            @SerializedName("expiredWording")
            @Expose
            var expiredWording: String = "",
            @SerializedName("expiredTimer")
            @Expose
            var expiredTimer: Int = 0,
            @SerializedName("totalQuotaLeft")
            @Expose
            var totalQuotaLeft: Int = 0,
            @SerializedName("totalQuotaLeftWording")
            @Expose
            var totalQuotaLeftWording: String = "",
            @SerializedName("minPurchaseWording")
            @Expose
            var minPurchaseWording: String = ""
        )

        data class ProductList(
            @SerializedName("totalProduct")
            @Expose
            var totalProduct: Int = 0,
            @SerializedName("totalProductWording")
            @Expose
            var totalProductWording: String = "",
            @SerializedName("data")
            @Expose
            var data: List<Data> = listOf()
        ) {
            data class Data(
                @SerializedName("productID")
                @Expose
                var productID: String = "",
                @SerializedName("childIDs")
                @Expose
                var childIDs: List<String> = listOf(),
                @SerializedName("name")
                @Expose
                var name: String = "",
                @SerializedName("imageUrl")
                @Expose
                var imageUrl: String = "",
                @SerializedName("productUrl")
                @Expose
                var productUrl: String = "",
                @SerializedName("displayPrice")
                @Expose
                var displayPrice: String = "",
                @SerializedName("originalPrice")
                @Expose
                var originalPrice: String = "",
                @SerializedName("discountPercentage")
                @Expose
                var discountPercentage: String = "",
                @SerializedName("isShowFreeOngkir")
                @Expose
                var isShowFreeOngkir: Boolean = false,
                @SerializedName("freeOngkirPromoIcon")
                @Expose
                var freeOngkirPromoIcon: String = "",
                @SerializedName("isSoldOut")
                @Expose
                var isSoldOut: Boolean = false,
                @SerializedName("rating")
                @Expose
                var rating: Int = 0,
                @SerializedName("averageRating")
                @Expose
                var averageRating: Double = 0.0,
                @SerializedName("totalReview")
                @Expose
                var totalReview: String = "",
                @SerializedName("cashback")
                @Expose
                var cashback: Int = 0,
                @SerializedName("city")
                @Expose
                var city: String = "",
                @SerializedName("minimumOrder")
                @Expose
                var minimumOrder: Int = 0,
                @SerializedName("labelGroups")
                @Expose
                var labelGroups: List<LabelGroups> = listOf(),
                @SerializedName("stock")
                @Expose
                var stock: Int = 0,
                @SerializedName("finalPrice")
                @Expose
                var finalPrice: String = "",
                @SerializedName("productInCart")
                @Expose
                var productInCart: ProductInCart = ProductInCart()
            ) {
                fun isVariant(): Boolean {
                    return childIDs.isNotEmpty()
                }

                data class LabelGroups(
                    @SerializedName("position")
                    @Expose
                    var position: String = "",
                    @SerializedName("title")
                    @Expose
                    var title: String = "",
                    @SerializedName("type")
                    @Expose
                    var type: String = "",
                    @SerializedName("url")
                    @Expose
                    var url: String = ""
                )

                data class ProductInCart(
                    @SerializedName("productID")
                    @Expose
                    var productId: String = "",
                    @SerializedName("qty")
                    @Expose
                    var qty: Int = 0
                )
            }
        }

        data class Error(
            @SerializedName("Message")
            @Expose
            var message: String = "",
            @SerializedName("Description")
            @Expose
            var description: String = "",
            @SerializedName("CtaText")
            @Expose
            var ctaText: String = "",
            @SerializedName("CtaLink")
            @Expose
            var ctaLink: String = "",
        )
    }


}