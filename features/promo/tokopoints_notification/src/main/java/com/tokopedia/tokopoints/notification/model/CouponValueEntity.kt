package com.tokopedia.tokopoints.notification.model

import android.os.Parcelable
import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CouponValueEntity(

    @SerializedName(value = "imageUrlMobile", alternate = ["image_url_mobile", "imageURLMobile"])
    var imageUrlMobile: String? = null,

    @SerializedName(value = "subTitle", alternate = ["sub_title", "subtitle"])
    var subTitle: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("expired")
    var expired: String? = null,

    ):Parcelable {
    val isEmpty: Boolean
        get() = (TextUtils.isEmpty(title)
                || TextUtils.isEmpty(imageUrlMobile))
}