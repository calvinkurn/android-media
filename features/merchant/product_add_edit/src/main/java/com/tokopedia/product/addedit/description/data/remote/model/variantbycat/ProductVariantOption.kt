package com.tokopedia.product.addedit.description.data.remote.model.variantbycat

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by hendry on 8/14/2017.
 */

@Parcelize
class ProductVariantOption(
        @SerializedName("value_id")
        @Expose
        var valueId: Int = 0,
        @SerializedName("value")
        @Expose
        var itemId: String? = null,
        @SerializedName("hex_code")
        @Expose
        var hexCode: String? = null,
        @SerializedName("icon")
        @Expose
        var icon: String? = null
): Parcelable {
    val id: String
        get() = valueId.toString()

    val type: Int
        get() = TYPE

    fun getValue(): String? {
        return itemId
    }

    companion object {
        const val TYPE = 199349
    }
}
