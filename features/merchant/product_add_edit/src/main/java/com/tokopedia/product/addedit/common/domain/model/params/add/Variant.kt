package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Variant (

        @SerializedName("sizeChart")
        @Expose
        var sizeChart: Picture? = null,
        @SerializedName("products")
        @Expose
        var products: Products? = null,
        @SerializedName("selections")
        @Expose
        var selections: Selections? = null

) : Parcelable
