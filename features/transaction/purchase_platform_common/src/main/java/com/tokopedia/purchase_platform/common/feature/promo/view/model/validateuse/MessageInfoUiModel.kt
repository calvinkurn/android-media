package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

data class MessageInfoUiModel(
	var detail: String = "",
	var message: String = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString() ?: "",
			parcel.readString() ?: "")

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(detail)
		parcel.writeString(message)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<MessageInfoUiModel> {
		override fun createFromParcel(parcel: Parcel): MessageInfoUiModel {
			return MessageInfoUiModel(parcel)
		}

		override fun newArray(size: Int): Array<MessageInfoUiModel?> {
			return arrayOfNulls(size)
		}
	}
}