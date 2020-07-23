package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

data class ConversionRateUiModel(
	var pointsCoefficient: Int? = -1,
	var rate: Int? = -1,
	var externalCurrencyCoefficient: Int? = -1
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(pointsCoefficient)
		parcel.writeValue(rate)
		parcel.writeValue(externalCurrencyCoefficient)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<ConversionRateUiModel> {
		override fun createFromParcel(parcel: Parcel): ConversionRateUiModel {
			return ConversionRateUiModel(parcel)
		}

		override fun newArray(size: Int): Array<ConversionRateUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
