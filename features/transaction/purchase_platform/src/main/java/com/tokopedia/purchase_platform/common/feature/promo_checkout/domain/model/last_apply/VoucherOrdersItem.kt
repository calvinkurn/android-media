package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcel
import android.os.Parcelable

data class VoucherOrdersItem(
	val code: String = "",
	val message: Message = Message()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString() ?: "",
			parcel.readParcelable(Message::class.java.classLoader)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(code)
		parcel.writeParcelable(message, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<VoucherOrdersItem> {
		override fun createFromParcel(parcel: Parcel): VoucherOrdersItem {
			return VoucherOrdersItem(parcel)
		}

		override fun newArray(size: Int): Array<VoucherOrdersItem?> {
			return arrayOfNulls(size)
		}
	}
}
