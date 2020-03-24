package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PictureId (
        @SerializedName("uploadIds")
        @Expose
        var uploadId: String = ""
) : Parcelable

@Parcelize
data class Pictures (
        @SerializedName("data")
        @Expose
        var data: List<PictureId> = emptyList()
) : Parcelable