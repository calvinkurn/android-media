package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

data class PromoTypeUiModel(
	var isExclusiveShipping: Boolean = false,
	var isBebasOngkir: Boolean = false
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readValue(Boolean::class.java.classLoader) as Boolean,
			parcel.readValue(Boolean::class.java.classLoader) as Boolean)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(isExclusiveShipping)
		parcel.writeValue(isBebasOngkir)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<PromoTypeUiModel> {
		override fun createFromParcel(parcel: Parcel): PromoTypeUiModel {
			return PromoTypeUiModel(parcel)
		}

		override fun newArray(size: Int): Array<PromoTypeUiModel?> {
			return arrayOfNulls(size)
		}
	}
}