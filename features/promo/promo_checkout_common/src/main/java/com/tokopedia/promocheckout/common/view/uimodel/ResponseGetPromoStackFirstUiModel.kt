package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

data class ResponseGetPromoStackFirstUiModel(
		val status: String = "",
		val data: DataUiModel
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readParcelable(DataUiModel::class.java.classLoader)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(status)
		parcel.writeParcelable(data, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<ResponseGetPromoStackFirstUiModel> {
		override fun createFromParcel(parcel: Parcel): ResponseGetPromoStackFirstUiModel {
			return ResponseGetPromoStackFirstUiModel(parcel)
		}

		override fun newArray(size: Int): Array<ResponseGetPromoStackFirstUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
