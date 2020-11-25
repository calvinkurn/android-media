package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

data class BenefitSummaryInfoUiModel(
	var finalBenefitAmountStr: String = "",
	var finalBenefitAmount: Int = -1,
	var finalBenefitText: String = "",
	var summaries: List<SummariesItemUiModel> = listOf()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString() ?: "",
			parcel.readValue(Int::class.java.classLoader) as Int,
			parcel.readString() ?: "",
			parcel.createTypedArrayList(SummariesItemUiModel) ?: emptyList())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(finalBenefitAmountStr)
		parcel.writeValue(finalBenefitAmount)
		parcel.writeString(finalBenefitText)
		parcel.writeTypedList(summaries)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<BenefitSummaryInfoUiModel> {
		override fun createFromParcel(parcel: Parcel): BenefitSummaryInfoUiModel {
			return BenefitSummaryInfoUiModel(parcel)
		}

		override fun newArray(size: Int): Array<BenefitSummaryInfoUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
