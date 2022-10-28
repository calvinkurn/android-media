package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Preorder (
    @SerializedName("duration")
    var duration: Int? = 0,
    @SerializedName("timeUnit")
    var timeUnit: String? = "DAY",
    @SerializedName("isActive")
    var isActive: Boolean? = false
) : Parcelable
