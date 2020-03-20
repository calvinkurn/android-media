package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Menu (

    @SerializedName("menuID")
    @Expose
    var menuID: String? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null

) : Parcelable
