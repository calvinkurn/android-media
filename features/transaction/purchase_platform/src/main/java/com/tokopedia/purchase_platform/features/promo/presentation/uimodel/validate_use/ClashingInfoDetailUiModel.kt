package com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use

import android.os.Parcel
import android.os.Parcelable

data class ClashingInfoDetailUiModel(
		var isClashedPromos: Boolean? = false,
		var options: List<PromoClashOptionUiModel> = emptyList(),
		var clashReason: String = "",
		var clashMessage: String = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.createTypedArrayList(PromoClashOptionUiModel),
			parcel.readString(),
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(isClashedPromos)
		parcel.writeTypedList(options)
		parcel.writeString(clashReason)
		parcel.writeString(clashMessage)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<ClashingInfoDetailUiModel> {
		override fun createFromParcel(parcel: Parcel): ClashingInfoDetailUiModel {
			return ClashingInfoDetailUiModel(parcel)
		}

		override fun newArray(size: Int): Array<ClashingInfoDetailUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
