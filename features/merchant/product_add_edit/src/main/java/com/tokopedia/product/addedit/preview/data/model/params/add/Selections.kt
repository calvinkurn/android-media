package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Selection (

    @SerializedName("variantID")
    @Expose
    var id: String = "",
    @SerializedName("unitID")
    @Expose
    var unitId: String = "",
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("options")
    @Expose
    var options: List<Option> = emptyList()

) : Parcelable
