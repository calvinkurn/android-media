package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

data class BenefitSummaryInfoUiModel(
		var finalBenefitText: String = "",
		var finalBenefitAmount: String = "",
		var summaries: List<SummariesUiModel> = emptyList()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString(),
			parcel.createTypedArrayList(SummariesUiModel)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(finalBenefitText)
		parcel.writeString(finalBenefitAmount)
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
