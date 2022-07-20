package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Picture (
        @SerializedName("description")
        var description: String = "",
        @SerializedName("fileName")
        var fileName: String = "",
        @SerializedName("filePath")
        var filePath: String = "",
        @SerializedName("picID")
        var picID: String = "0",
        @SerializedName("isFromIG")
        var isFromIG: Boolean = false,
        @SerializedName("width")
        var width: Int = 0,
        @SerializedName("height")
        var height: Int = 0,
        @SerializedName("uploadIds")
        var uploadId: String = ""
) : Parcelable

@Parcelize
data class Pictures (
        @SerializedName("data")
        var data: List<Picture> = emptyList()
) : Parcelable