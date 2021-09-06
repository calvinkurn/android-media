package com.tokopedia.checkout.data.model.request.checkout.old

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class DataCheckoutRequest(
        @SerializedName("address_id")
        var addressId: String? = null,
        @SerializedName("shop_products")
        var shopProducts: List<ShopProductCheckoutRequest>? = ArrayList()
) : Parcelable