package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcel
import android.os.Parcelable

data class Message(
	val color: String = "",
	val state: String = "",
	val text: String = ""
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

	companion object CREATOR : Parcelable.Creator<Message> {
		override fun createFromParcel(parcel: Parcel): Message {
			return Message(parcel)
		}

		override fun newArray(size: Int): Array<Message?> {
			return arrayOfNulls(size)
		}
	}
}
