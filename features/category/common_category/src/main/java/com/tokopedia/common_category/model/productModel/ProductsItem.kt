package com.tokopedia.common_category.model.productModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common_category.factory.ProductTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductsItem(

        @field:SerializedName("imageURL500")
        val imageURL500: String? = null,

        @field:SerializedName("imageURL700")
        var imageURL700: String = "",

        @SerializedName("shop")
        val shop: Shop = Shop(),

        @field:SerializedName("originalPrice")
        var originalPrice: String = "",

        @field:SerializedName("wishlist")
        var wishlist: Boolean = false,

        @field:SerializedName("isWishListEnabled")
        var isWishListEnabled: Boolean = true,

        @field:SerializedName("rating")
        var rating: Int = 0,

        @field:SerializedName("categoryName")
        val categoryName: String? = null,

        @field:SerializedName("discountPercentage")
        var discountPercentage: Int = 0,

        @field:SerializedName("countReview")
        var countReview: Int = 0,

        @field:SerializedName("price")
        var price: String = "",

        @field:SerializedName("imageURL")
        var imageURL: String = "",

        @field:SerializedName("id")
        var id: String? = null,

        @field:SerializedName("categoryBreadcrumb")
        val categoryBreadcrumb: String? = null,

        @field:SerializedName("isFeatured")
        val isFeatured: Int? = null,

        @field:SerializedName("stock")
        val stock: Int? = null,

        @field:SerializedName("categoryID")
        var categoryID: Int? = null,

        @field:SerializedName("GAKey")
        val gAKey: String? = null,

        @field:SerializedName("courierCount")
        val courierCount: Int? = null,

        @field:SerializedName("url")
        val url: String? = null,

        @field:SerializedName("labels")
        val labels: List<LabelsItem?> = arrayListOf(),

        @field:SerializedName("badges")
        var badges: List<BadgesItem> = arrayListOf(),

        @field:SerializedName("condition")
        val condition: Int? = null,

        @field:SerializedName("labelGroups")
        val labelGroups: List<LabelGroupsItem?> = arrayListOf(),

        @field:SerializedName("name")
        var name: String = "",

        @field:SerializedName("category")
        val category: Int? = null,

        @field:SerializedName("priceRange")
        var priceRange: String = "",

        @field:SerializedName("imageURL300")
        var imageURL300: String? = null,

        @field:SerializedName("preorder")
        val preorder: Boolean? = null,

        @field:SerializedName("isTopAds")
        var isTopAds: Boolean = false,

        @field:SerializedName("productImpTrackingUrl")
        var productImpTrackingUrl: String = "",

        @field:SerializedName("productClickTrackingUrl")
        var productClickTrackingUrl: String = "",

        @field:SerializedName("productWishlistTrackingUrl")
        var productWishlistTrackingUrl: String = "",

        @field:SerializedName("adapterPosition")
        var adapter_position: Int = 0,

        @field:SerializedName("free_ongkir")
        var freeOngkir: FreeOngkir? = null,

        @field:SerializedName("dimension")
        var dimension: String? = null



) : ImpressHolder(), Parcelable, Visitable<ProductTypeFactory> {


    override fun type(typeFactory: ProductTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}

@Parcelize
data class FreeOngkir(
        @SerializedName("is_active")
        val isActive: Boolean,
        @SerializedName("img_url")
        val imageUrl: String
) : Parcelable