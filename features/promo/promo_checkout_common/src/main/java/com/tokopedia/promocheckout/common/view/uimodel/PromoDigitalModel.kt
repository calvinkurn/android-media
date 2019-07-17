package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

data class PromoDigitalModel(
	var categoryId: Int = 0,
	var productId: Int = 0,
	var clientNumber: String = "",
	var price: Long = 0
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readInt(),
			parcel.readInt(),
			parcel.readString() ?: "",
			parcel.readLong())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(productId)
		parcel.writeString(clientNumber)
		parcel.writeLong(price)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<PromoDigitalModel> {
		override fun createFromParcel(parcel: Parcel): PromoDigitalModel {
			return PromoDigitalModel(parcel)
		}

		override fun newArray(size: Int): Array<PromoDigitalModel?> {
			return arrayOfNulls(size)
		}
	}
}
