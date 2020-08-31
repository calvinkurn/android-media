package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

data class ErrorDetailUiModel(
	var message: String = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(parcel.readString() ?: "")

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(message)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<ErrorDetailUiModel> {
		override fun createFromParcel(parcel: Parcel): ErrorDetailUiModel {
			return ErrorDetailUiModel(parcel)
		}

		override fun newArray(size: Int): Array<ErrorDetailUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
