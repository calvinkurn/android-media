package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

data class DetailsItemUiModel(
	var amount: Int = -1,
	var sectionName: String = "",
	var description: String = "",
	var type: String = "",
	var amountStr: String = "",
	var points: Int = -1,
	var pointsStr: String = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readValue(Int::class.java.classLoader) as Int,
			parcel.readString() ?: "",
			parcel.readString() ?: "",
			parcel.readString() ?: "",
			parcel.readString() ?: "",
			parcel.readValue(Int::class.java.classLoader) as Int,
			parcel.readString() ?: "")

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(amount)
		parcel.writeString(sectionName)
		parcel.writeString(description)
		parcel.writeString(type)
		parcel.writeString(amountStr)
		parcel.writeValue(points)
		parcel.writeString(pointsStr)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<DetailsItemUiModel> {
		override fun createFromParcel(parcel: Parcel): DetailsItemUiModel {
			return DetailsItemUiModel(parcel)
		}

		override fun newArray(size: Int): Array<DetailsItemUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
