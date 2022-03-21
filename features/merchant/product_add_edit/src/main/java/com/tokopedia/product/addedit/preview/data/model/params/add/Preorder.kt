package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Preorder (
    @SerializedName("duration")
    @Expose
    var duration: Int? = 0,
    @SerializedName("timeUnit")
    @Expose
    var timeUnit: String? = "DAY",
    @SerializedName("isActive")
    @Expose
    var isActive: Boolean? = false
) : Parcelable
