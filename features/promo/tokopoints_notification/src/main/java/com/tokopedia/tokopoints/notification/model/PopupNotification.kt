package com.tokopedia.tokopoints.notification.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PopupNotification(

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("text")
    var text: String? = null,

    @SerializedName("imageURL")
    var imageURL: String? = null,

    @SerializedName("buttonText")
    var buttonText: String? = null,

    @SerializedName("buttonURL")
    var buttonURL: String? = null,

    @SerializedName("appLink")
    var appLink: String? = null,

    @SerializedName("notes")
    var notes: String? = null,

    @SerializedName("sender")
    var sender: String? = null,

    @SerializedName("catalog")
    var catalog: CouponValueEntity? = CouponValueEntity()

):Parcelable {
    val isEmpty: Boolean
        get() = if (catalog == null || catalog?.isEmpty == true) {
            title?.isEmpty() ?: false
        } else catalog?.isEmpty ?: false
}