package com.tokopedia.atc_common.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.atc_common.data.model.request.chosenaddress.ChosenAddressAddToCart

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
        var notes: String = "",
        @SerializedName("chosen_address")
        @Expose
        var chosenAddressAddToCart: ChosenAddressAddToCart? = null,

        // analytics data
        var productName: String = "",
        var category: String = "",
        var price: String = "",
        var userId: String = "",
        var categoryLevel1Id: String = "",
        var categoryLevel1Name: String = "",
        var categoryLevel2Id: String = "",
        var categoryLevel2Name: String = "",
        var categoryLevel3Id: String = "",
        var categoryLevel3Name: String = ""
)