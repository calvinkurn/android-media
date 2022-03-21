package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Variant (
        @SerializedName("selections")
        @Expose
        var selections: List<Selection>? = emptyList(),
        @SerializedName("products")
        @Expose
        var products: List<Product>? = emptyList(),
        @SerializedName("sizeChart")
        @Expose
        var sizeChart: List<Picture>? = emptyList()
): Parcelable
