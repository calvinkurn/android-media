package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductEtalase (
    @SerializedName("menuID")
    var menuID: String = "0",
    @SerializedName("name")
    var name: String = ""
): Parcelable
