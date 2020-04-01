package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

data class ResponseGetPromoStackUiModel(
		var status: String = "",
		var message: List<String> = ArrayList(),
		var data: DataUiModel = DataUiModel()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.createStringArrayList(),
			parcel.readParcelable(DataUiModel::class.java.classLoader)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(status)
		parcel.writeParcelable(data, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<ResponseGetPromoStackUiModel> {
		override fun createFromParcel(parcel: Parcel): ResponseGetPromoStackUiModel {
			return ResponseGetPromoStackUiModel(parcel)
		}

		override fun newArray(size: Int): Array<ResponseGetPromoStackUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
