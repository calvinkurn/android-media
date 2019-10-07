package com.tokopedia.discovery.categoryrevamp.data.productModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class LabelGroupsItem(

	@field:SerializedName("position")
	val position: String? = null,

	@field:SerializedName("type")
	val type: String = "",

	@field:SerializedName("title")
	val title: String = ""
):Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString(),
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(position)
		parcel.writeString(type)
		parcel.writeString(title)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<LabelGroupsItem> {
		override fun createFromParcel(parcel: Parcel): LabelGroupsItem {
			return LabelGroupsItem(parcel)
		}

		override fun newArray(size: Int): Array<LabelGroupsItem?> {
			return arrayOfNulls(size)
		}
	}
}