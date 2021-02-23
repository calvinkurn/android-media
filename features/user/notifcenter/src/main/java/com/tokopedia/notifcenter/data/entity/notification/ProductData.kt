package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.notifcenter.data.entity.Campaign
import com.tokopedia.notifcenter.data.entity.Variant

data class ProductData(
        @SerializedName("campaign")
        val campaign: Campaign = Campaign(),
        @SerializedName("count_review")
        val countReview: Int = 0,
        @SerializedName("currency")
        val currency: String = "",
        @SerializedName("department_id")
        val departmentId: Long = 0,
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("is_buyable")
        val isBuyable: Boolean = false,
        @SerializedName("is_reminded")
        val isReminded: Boolean = false,
        @SerializedName("is_show")
        val isShow: Boolean = false,
        @SerializedName("is_topads")
        val isTopads: Boolean = false,
        @SerializedName("is_wishlist")
        val isWishlist: Boolean = false,
        @SerializedName("labels")
        val labels: List<Any> = listOf(),
        @SerializedName("min_order")
        val minOrder: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("price")
        val price: Double = 0.0,
        @SerializedName("price_fmt")
        val priceFmt: String = "",
        @SerializedName("price_idr")
        val priceIdr: Double = 0.0,
        @SerializedName("product_id")
        val productId: String = "0",
        @SerializedName("rating")
        val rating: Int = 0,
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("type_button")
        val typeButton: Int = 0,
        @SerializedName("url")
        val url: String = "",
        @SerializedName("variant")
        val variant: List<Variant> = listOf(),
        @SerializedName("has_reminder")
        var hasReminder: Boolean = false
) {
    var loadingReminderState: Boolean = false

    val impressHolder = ImpressHolder()

    fun isBuyButton() = typeButton == BUTTON_TYPE_BUY
    fun isReminderButton() = typeButton == BUTTON_TYPE_REMINDER
    fun isEmptyButton() = typeButton == BUTTON_TYPE_EMPTY_STOCK

    fun hasEmptyStock(): Boolean {
        return isShow
    }

    fun update(productData: ProductData) {
        loadingReminderState = productData.loadingReminderState
        hasReminder = productData.hasReminder
    }

    fun update(isLoading: Boolean, hasReminder: Boolean?) {
        this.loadingReminderState = isLoading
        hasReminder?.let {
            this.hasReminder = it
        }
    }

    fun hasFreeShipping(): Boolean {
        return shop.freeShippingIcon.isNotEmpty()
    }

    companion object {
        const val BUTTON_TYPE_BUY = 0
        const val BUTTON_TYPE_REMINDER = 1
        const val BUTTON_TYPE_EMPTY_STOCK = 2
    }
}