package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video (
    
    @SerializedName("source")
    @Expose
    var source: String? = null,
    @SerializedName("url")
    @Expose
    var url: String? = null

) : Parcelable

@Parcelize
data class Videos (
        @SerializedName("data")
        @Expose
        var data: List<Video> = emptyList()
) : Parcelable