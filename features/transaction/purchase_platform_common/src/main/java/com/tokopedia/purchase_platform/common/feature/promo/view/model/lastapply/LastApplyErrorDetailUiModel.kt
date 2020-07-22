package com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply

import android.os.Parcel
import android.os.Parcelable

data class LastApplyErrorDetailUiModel(
	var message: String = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(parcel.readString() ?: "") {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(message)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<LastApplyErrorDetailUiModel> {
		override fun createFromParcel(parcel: Parcel): LastApplyErrorDetailUiModel {
			return LastApplyErrorDetailUiModel(parcel)
		}

		override fun newArray(size: Int): Array<LastApplyErrorDetailUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
