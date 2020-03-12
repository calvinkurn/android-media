package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcel
import android.os.Parcelable

data class MessageInfo(
	val detail: String = "",
	val message: String = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString() ?: "",
			parcel.readString() ?: "") {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(detail)
		parcel.writeString(message)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<MessageInfo> {
		override fun createFromParcel(parcel: Parcel): MessageInfo {
			return MessageInfo(parcel)
		}

		override fun newArray(size: Int): Array<MessageInfo?> {
			return arrayOfNulls(size)
		}
	}
}
