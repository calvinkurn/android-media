package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Preorder (
    
    @SerializedName("duration")
    @Expose
    var duration: Int? = null,
    @SerializedName("timeUnit")
    @Expose
    var timeUnit: String? = null,
    @SerializedName("isActive")
    @Expose
    var isActive: Boolean? = null

) : Parcelable
