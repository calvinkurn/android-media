package com.tokopedia.promocheckout.common.domain.model.deals

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DealsVerifyBody(
        @SerializedName("cart_items")
        @Expose
        val cartItems: List<CartItemVerify> = arrayListOf(),
        @SerializedName("promocode")
        @Expose
        var promocode: String = "",
        @SerializedName("promocode")
        @Expose
        var order_title: String = ""
) : Parcelable

@Parcelize
data class CartItemVerify(
        @SerializedName("configuration")
        @Expose
        val configuration: ConfigurationVerify = ConfigurationVerify(),
        @SerializedName("meta_data")
        @Expose
        val metaData: MetaDataVerify = MetaDataVerify(),
        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0
) : Parcelable

@Parcelize
data class ConfigurationVerify(
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("sub_config")
        @Expose
        val subConfig: SubConfigVerify = SubConfigVerify()
) : Parcelable

@Parcelize
data class SubConfigVerify(
        @SerializedName("expiry_date")
        @Expose
        val expiryDate: String = ""
) : Parcelable

@Parcelize
data class MetaDataVerify(
        @SerializedName("entity_category_id")
        @Expose
        val entityCategoryId: Int = 0,
        @SerializedName("entity_product_id")
        @Expose
        val entityProductId: Int = 0,
        @SerializedName("entity_product_url")
        @Expose
        val entityProductUrl: String = "",
        @SerializedName("entity_start_time")
        @Expose
        val entityStartTime: String = "",
        @SerializedName("total_ticket_count")
        @Expose
        val totalTicketCount: Int = 0,
        @SerializedName("total_ticket_price")
        @Expose
        val totalTicketPrice: Int = 0
) : Parcelable