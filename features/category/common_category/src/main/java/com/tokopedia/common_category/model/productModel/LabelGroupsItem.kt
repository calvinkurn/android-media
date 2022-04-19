package com.tokopedia.common_category.model.productModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LabelGroupsItem(

	@field:SerializedName("position")
	val position: String? = null,

	@field:SerializedName("type")
	val type: String = "",

	@field:SerializedName("title")
	val title: String = ""
):Parcelable