package com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply

import android.os.Parcel
import android.os.Parcelable

data class LastApplyMessageUiModel(
	var color: String = "",
	var state: String = "",
	var text: String = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString() ?: "",
			parcel.readString() ?: "",
			parcel.readString() ?: "") {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(color)
		parcel.writeString(state)
		parcel.writeString(text)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<LastApplyMessageUiModel> {
		override fun createFromParcel(parcel: Parcel): LastApplyMessageUiModel {
			return LastApplyMessageUiModel(parcel)
		}

		override fun newArray(size: Int): Array<LastApplyMessageUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
