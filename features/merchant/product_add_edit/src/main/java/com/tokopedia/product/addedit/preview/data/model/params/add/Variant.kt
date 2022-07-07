package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Variant (
        @SerializedName("selections")
        var selections: List<Selection>? = emptyList(),
        @SerializedName("products")
        var products: List<Product>? = emptyList(),
        @SerializedName("sizeChart")
        var sizeChart: List<Picture>? = emptyList()
): Parcelable
