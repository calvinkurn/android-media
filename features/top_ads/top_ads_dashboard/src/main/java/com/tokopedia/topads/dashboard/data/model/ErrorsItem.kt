package com.tokopedia.topads.dashboard.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorsItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("title")
	val title: String? = null
):Parcelable