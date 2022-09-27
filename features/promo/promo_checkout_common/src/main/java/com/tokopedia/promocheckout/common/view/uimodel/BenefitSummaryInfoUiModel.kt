package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

data class BenefitSummaryInfoUiModel(
		var finalBenefitText: String = "",
		var finalBenefitAmountStr: String = "",
		var finalBenefitAmount: Long = 0,
		var summaries: List<SummariesUiModel> = emptyList()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString() ?: "",
			parcel.readString() ?: "",
			parcel.readLong(),
			parcel.createTypedArrayList(SummariesUiModel) ?: emptyList()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(finalBenefitText)
		parcel.writeString(finalBenefitAmountStr)
		parcel.writeLong(finalBenefitAmount)
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
