package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcel
import android.os.Parcelable

data class LastApplyVoucherOrdersItemUiModel(
	var code: String = "",
	var message: LastApplyMessageUiModel = LastApplyMessageUiModel()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString() ?: "",
			parcel.readParcelable(LastApplyMessageUiModel::class.java.classLoader)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(code)
		parcel.writeParcelable(message, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<LastApplyVoucherOrdersItemUiModel> {
		override fun createFromParcel(parcel: Parcel): LastApplyVoucherOrdersItemUiModel {
			return LastApplyVoucherOrdersItemUiModel(parcel)
		}

		override fun newArray(size: Int): Array<LastApplyVoucherOrdersItemUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
