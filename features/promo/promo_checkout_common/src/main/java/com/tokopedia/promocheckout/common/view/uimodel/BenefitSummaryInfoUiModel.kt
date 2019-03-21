package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.BenefitSummaryInfo

data class BenefitSummaryInfoUiModel(
		var finalBenefitText: String = "",
		var finalBenefitAmount: String = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(finalBenefitText)
		parcel.writeString(finalBenefitAmount)
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
