package com.tokopedia.discovery.categoryrevamp.data.productModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.categoryrevamp.data.typefactory.product.ProductTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

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
        var id: Int? = null,

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
        var freeOngkir: FreeOngkir? = null


) : ImpressHolder(), Parcelable, Visitable<ProductTypeFactory> {


    override fun type(typeFactory: ProductTypeFactory?): Int {
        return typeFactory!!.type(this)
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Shop::class.java.classLoader),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as Boolean,
            parcel.readInt(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as Int,
            parcel.readValue(Int::class.java.classLoader) as Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.createTypedArrayList(LabelsItem.CREATOR) ?: arrayListOf(),
            parcel.createTypedArrayList(BadgesItem.CREATOR) ?: arrayListOf(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.createTypedArrayList(LabelGroupsItem.CREATOR) ?: arrayListOf(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.imageURL500)
        dest?.writeString(this.imageURL700)
        dest?.writeParcelable(this.shop, flags)
        dest?.writeString(this.imageURL500)
        dest?.writeString(this.originalPrice)
        dest?.writeValue(this.wishlist)
        dest?.writeValue(this.isWishListEnabled)
        dest?.writeInt(this.rating ?: 0)
        dest?.writeString(this.categoryName)
        dest?.writeInt(this.discountPercentage)
        dest?.writeInt(this.countReview ?: 0)
        dest?.writeString(this.price)
        dest?.writeString(this.imageURL)
        dest?.writeInt(this.id ?: 0)
        dest?.writeString(this.categoryBreadcrumb)
        dest?.writeInt(this.isFeatured ?: 0)
        dest?.writeInt(this.stock ?: 0)
        dest?.writeInt(this.categoryID ?: 0)
        dest?.writeString(this.gAKey)
        dest?.writeInt(this.courierCount ?: 0)
        dest?.writeString(this.url)
        dest?.writeTypedList(this.labels)
        dest?.writeTypedList(this.badges)
        dest?.writeInt(this.condition ?: 0)
        dest?.writeTypedList(this.labelGroups)
        dest?.writeString(this.name)
        dest?.writeInt(this.category ?: 0)
        dest?.writeString(this.priceRange)
        dest?.writeString(this.imageURL300)
        dest?.writeValue(this.preorder)
        dest?.writeValue(this.isTopAds)
        dest?.writeValue(this.productImpTrackingUrl)
        dest?.writeValue(this.productClickTrackingUrl)
        dest?.writeValue(this.productWishlistTrackingUrl)
        dest?.writeInt(this.adapter_position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductsItem> {
        override fun createFromParcel(parcel: Parcel): ProductsItem {
            return ProductsItem(parcel)
        }

        override fun newArray(size: Int): Array<ProductsItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class FreeOngkir(
        @SerializedName("is_active")
        val isActive: Boolean,
        @SerializedName("img_url")
        val imageUrl: String
)