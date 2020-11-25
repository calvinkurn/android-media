package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

data class TickerInfoUiModel(
	var uniqueId: String? = "",
	var statusCode: Int? = -1,
	var message: String = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString() ?: "")

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(uniqueId)
		parcel.writeValue(statusCode)
		parcel.writeString(message)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<TickerInfoUiModel> {
		override fun createFromParcel(parcel: Parcel): TickerInfoUiModel {
			return TickerInfoUiModel(parcel)
		}

		override fun newArray(size: Int): Array<TickerInfoUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
