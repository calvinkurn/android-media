package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Selections (

    @SerializedName("variantID")
    @Expose
    var variantID: String? = null,
    @SerializedName("unitID")
    @Expose
    var unitID: String? = null,
    @SerializedName("options")
    @Expose
    var options: Options? = null

) : Parcelable
