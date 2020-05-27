package com.tokopedia.product.addedit.description.data.remote.model.variantbycat

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by hendry on 8/14/2017.
 */
@Parcelize
class ProductVariantUnit (
        @SerializedName("unit_id")
        @Expose
        var unitId: Int = 0,
        @SerializedName("name")
        @Expose
        var name: String? = null,
        @SerializedName("short_name")
        @Expose
        var shortName: String? = null,
        @SerializedName("values")
        @Expose
        var productVariantOptionList: List<ProductVariantOption> = emptyList()
) : Parcelable
