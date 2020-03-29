package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Variant (

        @SerializedName("selections")
        @Expose
        var selections: List<Selection> = emptyList(),
        @SerializedName("products")
        @Expose
        var products: List<Product> = emptyList(),
        @SerializedName("sizeChart")
        @Expose
        var sizeChart: List<PictureVariant> = emptyList()

) : Parcelable
