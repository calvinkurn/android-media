package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

data class MessageUiModel(
	var color: String = "",
	var state: String = "",
	var text: String = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString(),
			parcel.readString()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(color)
		parcel.writeString(state)
		parcel.writeString(text)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<MessageUiModel> {
		override fun createFromParcel(parcel: Parcel): MessageUiModel {
			return MessageUiModel(parcel)
		}

		override fun newArray(size: Int): Array<MessageUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
