package com.tokopedia.notifications.model.payload_extra

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Topchat(
    @SerializedName("messageId")
    @Expose
    var messageId: String? = "",

    @SerializedName("fromShopId")
    @Expose
    var fromShopId: String? = "",

    @SerializedName("fromShopName")
    @Expose
    var fromShopName: String? = ""
) : Parcelable
