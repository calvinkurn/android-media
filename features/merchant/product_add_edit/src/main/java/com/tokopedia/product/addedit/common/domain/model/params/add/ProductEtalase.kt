package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ProductEtalase (

    @SerializedName("menuID")
    @Expose
    var menuID: String = "0",
    @SerializedName("name")
    @Expose
    var name: String = ""

) : Parcelable
