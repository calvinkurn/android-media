package com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse

import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Generated("com.robohorse.robopojogenerator")
@Parcelize
data class OrdersItem(

		@field:SerializedName("shipping_id")
        var shippingId: Int = 0,

		@field:SerializedName("shop_id")
        var shopId: Long = 0,

		@field:SerializedName("codes")
        var codes: MutableList<String> = mutableListOf(),

		@field:SerializedName("unique_id")
        var uniqueId: String = "",

		@field:SerializedName("sp_id")
        var spId: Int = 0,

		@field:SerializedName("product_details")
        var productDetails: List<ProductDetailsItem?> = listOf()
) : Parcelable