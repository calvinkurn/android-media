package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video (
    @SerializedName("source")
    var source: String? = null,
    @SerializedName("url")
    var url: String? = null
): Parcelable

@Parcelize
data class Videos (
    @SerializedName("data")
    var data: List<Video> = emptyList()
): Parcelable