package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

data class DiscountDetailsItemUiModel(
	var amount: Int? = -1,
	var dataType: String? = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(amount)
		parcel.writeString(dataType)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<DiscountDetailsItemUiModel> {
		override fun createFromParcel(parcel: Parcel): DiscountDetailsItemUiModel {
			return DiscountDetailsItemUiModel(parcel)
		}

		override fun newArray(size: Int): Array<DiscountDetailsItemUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
