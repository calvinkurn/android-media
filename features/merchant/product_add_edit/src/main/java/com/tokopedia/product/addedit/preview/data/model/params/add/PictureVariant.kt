package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by faisalramd on 2020-03-29.
 */

@Parcelize
data class PictureVariant (
        @SerializedName("id")
        @Expose
        var id: Long = 0,
        @SerializedName("description")
        @Expose
        var description: String = "",
        @SerializedName("file_path")
        @Expose
        var filePath: String = "",
        @SerializedName("file_name")
        @Expose
        var fileName: String = "",
        @SerializedName("x")
        @Expose
        var x: Long = 0,
        @SerializedName("y")
        @Expose
        var y: Long = 0,
        @SerializedName("is_from_ig")
        @Expose
        var isFromIg: Boolean = false,
        @SerializedName("image_id")
        @Expose
        var imageId: Int = 0
) : Parcelable