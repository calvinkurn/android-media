package com.tokopedia.shop_widget.mvc_locked_to_product.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
        var productList: ProductList = ProductList()
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
            var totalQuotaLeftWording: String = ""
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
                var productID: Int = 0,
                @SerializedName("childIDs")
                @Expose
                var childIDs: List<Int> = listOf(),
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
                @SerializedName("productInCart")
                @Expose
                var productInCart: Int = 0
            ) {
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
            }
        }
    }


}