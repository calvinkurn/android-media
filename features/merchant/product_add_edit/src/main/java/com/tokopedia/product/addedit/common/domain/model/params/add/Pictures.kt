package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Picture (
    
    @SerializedName("picID")
    @Expose
    var picID: String? = null,
    @SerializedName("description")
    @Expose
    var description: String? = null,
    @SerializedName("filePath")
    @Expose
    var filePath: String? = null,
    @SerializedName("fileName")
    @Expose
    var fileName: String? = null,
    @SerializedName("width")
    @Expose
    var width: Int? = null,
    @SerializedName("height")
    @Expose
    var height: Int? = null

) : Parcelable

@Parcelize
data class Pictures (
        @SerializedName("data")
        @Expose
        var data: List<Pictures> = emptyList()
) : Parcelable