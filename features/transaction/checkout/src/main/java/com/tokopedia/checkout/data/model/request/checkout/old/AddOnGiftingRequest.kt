package com.tokopedia.checkout.data.model.request.checkout.old

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnGiftingRequest (
        @SerializedName("item_type")
        var itemType: String = "",
        @SerializedName("item_id")
        var itemId: String = "",
        @SerializedName("item_qty")
        var itemQty: Int = 0,
        @SerializedName("item_metadata")
        var itemMetadata: String = "",
) : Parcelable