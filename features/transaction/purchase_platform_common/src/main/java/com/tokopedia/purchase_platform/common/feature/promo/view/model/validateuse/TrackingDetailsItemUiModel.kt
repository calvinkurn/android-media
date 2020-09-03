package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

data class TrackingDetailsItemUiModel(
	var promoDetailsTracking: String? = null,
	var productId: Int? = null,
	var promoCodesTracking: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(promoDetailsTracking)
		parcel.writeValue(productId)
		parcel.writeString(promoCodesTracking)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<TrackingDetailsItemUiModel> {
		override fun createFromParcel(parcel: Parcel): TrackingDetailsItemUiModel {
			return TrackingDetailsItemUiModel(parcel)
		}

		override fun newArray(size: Int): Array<TrackingDetailsItemUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
