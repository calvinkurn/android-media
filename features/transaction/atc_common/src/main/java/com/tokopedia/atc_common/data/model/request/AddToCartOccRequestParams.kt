package com.tokopedia.atc_common.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddToCartOccRequestParams(
        @SerializedName("product_id")
        @Expose
        var productId: String,
        @SerializedName("shop_id")
        @Expose
        var shopId: String,
        @SerializedName("quantity")
        @Expose
        var quantity: String,
        @SerializedName("warehouse_id")
        @Expose
        var warehouseId: String = "0",
        @SerializedName("lang")
        @Expose
        var lang: String = "id",
        @SerializedName("is_scp")
        @Expose
        var isScp: Boolean = false,
        @SerializedName("uc_params")
        @Expose
        var ucParam: String = "",
        @SerializedName("attribution")
        @Expose
        var attribution: String = "",
        @SerializedName("list_tracker")
        @Expose
        var listTracker: String = "",
        @SerializedName("notes")
        @Expose
        var notes: String = ""
)