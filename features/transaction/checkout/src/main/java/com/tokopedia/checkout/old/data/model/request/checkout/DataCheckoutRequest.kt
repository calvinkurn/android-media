package com.tokopedia.checkout.old.data.model.request.checkout

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class DataCheckoutRequest(
        @SerializedName("address_id")
        var addressId: String? = null,
        @SerializedName("shop_products")
        var shopProducts: List<ShopProductCheckoutRequest>? = ArrayList()
) : Parcelable