package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

data class BenefitProductDetailsItemUiModel(
	var cashbackAmountIdr: Int? = -1,
	var cashbackAmount: Int? = -1,
	var discountAmount: Int? = -1,
	var productId: Int? = -1,
	var isBebasOngkir: Boolean? = false
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(cashbackAmountIdr)
		parcel.writeValue(cashbackAmount)
		parcel.writeValue(discountAmount)
		parcel.writeValue(productId)
		parcel.writeValue(isBebasOngkir)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<BenefitProductDetailsItemUiModel> {
		override fun createFromParcel(parcel: Parcel): BenefitProductDetailsItemUiModel {
			return BenefitProductDetailsItemUiModel(parcel)
		}

		override fun newArray(size: Int): Array<BenefitProductDetailsItemUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
