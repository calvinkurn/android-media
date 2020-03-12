package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcel
import android.os.Parcelable

data class LastApplyAdditionalInfoUiModel(
		var messageInfo: LastApplyMessageInfoUiModel = LastApplyMessageInfoUiModel(),
		var errorDetail: LastApplyErrorDetailUiModel = LastApplyErrorDetailUiModel(),
		var emptyCartInfo: LastApplyEmptyCartInfoUiModel = LastApplyEmptyCartInfoUiModel()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readParcelable(LastApplyMessageInfoUiModel::class.java.classLoader) ?: LastApplyMessageInfoUiModel(),
			parcel.readParcelable(LastApplyErrorDetailUiModel::class.java.classLoader) ?: LastApplyErrorDetailUiModel(),
			parcel.readParcelable(LastApplyEmptyCartInfoUiModel::class.java.classLoader) ?: LastApplyEmptyCartInfoUiModel()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeParcelable(messageInfo, flags)
		parcel.writeParcelable(errorDetail, flags)
		parcel.writeParcelable(emptyCartInfo, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<LastApplyAdditionalInfoUiModel> {
		override fun createFromParcel(parcel: Parcel): LastApplyAdditionalInfoUiModel {
			return LastApplyAdditionalInfoUiModel(parcel)
		}

		override fun newArray(size: Int): Array<LastApplyAdditionalInfoUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
