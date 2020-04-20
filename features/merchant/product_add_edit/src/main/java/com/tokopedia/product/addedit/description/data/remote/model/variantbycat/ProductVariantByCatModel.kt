package com.tokopedia.product.addedit.description.data.remote.model.variantbycat

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by hendry on 8/14/2017.
 */

@Parcelize
class ProductVariantByCatModel(
        @SerializedName("variant_id")
        @Expose
        var variantId: Int = 0,
        @SerializedName("name")
        @Expose
        var name: String? = null,
        @SerializedName("identifier")
        @Expose
        var identifier: String?  = null,
        @SerializedName("status") // 2: level 1, 1: level 2
        @Expose
        var status: Int = 0,
        @SerializedName("has_unit")
        @Expose
        var hasUnit: Int = 0,
        @SerializedName("units")
        @Expose
        var unitList: List<ProductVariantUnit> = emptyList()
): Parcelable {
    val level: Int
        get() = if (status == STATUS_LEVEL_1) 1 else 2

    val isDataColorType: Boolean
        get() = variantId == COLOR_ID

    val isSizeIdentifier: Boolean
        get() = identifier!!.equals(SIZE_IDENTIFIER_STRING, ignoreCase = true)

    fun hasUnit(): Boolean {
        return hasUnit > 0 && unitList.isNotEmpty()
    }

    companion object {
        const val COLOR_ID = 1 // from API

        const val STATUS_LEVEL_1 = 2 // from API
        const val SIZE_IDENTIFIER_STRING = "size" // from API
    }

}
