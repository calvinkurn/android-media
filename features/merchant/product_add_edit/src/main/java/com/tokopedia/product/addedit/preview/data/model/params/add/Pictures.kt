package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Picture (
        @SerializedName("description")
        @Expose
        var description: String = "",
        @SerializedName("fileName")
        @Expose
        var fileName: String = "",
        @SerializedName("filePath")
        @Expose
        var filePath: String = "",
        @SerializedName("picID")
        @Expose
        var picID: String = "0",
        @SerializedName("isFromIG")
        @Expose
        var isFromIG: Boolean = false,
        @SerializedName("width")
        @Expose
        var width: Int = 0,
        @SerializedName("height")
        @Expose
        var height: Int = 0,
        @SerializedName("uploadIds")
        @Expose
        var uploadId: String = ""
) : Parcelable

@Parcelize
data class Pictures (
        @SerializedName("data")
        @Expose
        var data: List<Picture> = emptyList()
) : Parcelable