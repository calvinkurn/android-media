package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Selection (
    @SerializedName("variantID")
    var id: String = "",
    @SerializedName("unitID")
    var unitId: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("options")
    var options: List<Option> = emptyList()
): Parcelable
