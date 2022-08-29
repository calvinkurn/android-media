package com.tokopedia.tokopoints.notification.model.popupnotif

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PopupNotif(

	@SerializedName("buttonText")
	val buttonText: String? = null,

	@SerializedName("appLink")
	val appLink: String? = null,

	@SerializedName("titleHeader")
	val titleHeader: String? = null,

	@SerializedName("imageURL")
	val imageURL: String? = null,

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("buttonURL")
	val buttonURL: String? = null
):Parcelable

data class PopupNotifResponse(
	@SerializedName("tokopoints")
	val tokopoints: Tokopoints? = null
)

data class Tokopoints(
	@SerializedName("popupNotif")
	val popupNotif: PopupNotif? = null
)
