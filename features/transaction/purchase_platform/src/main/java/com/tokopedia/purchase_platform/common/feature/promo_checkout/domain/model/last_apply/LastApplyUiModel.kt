package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcel
import android.os.Parcelable

data class LastApplyUiModel(
		var codes: List<String> = emptyList(),
		var voucherOrders: List<LastApplyVoucherOrdersItemUiModel> = emptyList(),
		var additionalInfo: LastApplyAdditionalInfoUiModel = LastApplyAdditionalInfoUiModel(),
		var message: LastApplyMessageUiModel = LastApplyMessageUiModel(),
		var listRedPromos: List<String> = emptyList()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.createStringArrayList(),
			parcel.createTypedArrayList(LastApplyVoucherOrdersItemUiModel),
			parcel.readParcelable(LastApplyAdditionalInfoUiModel::class.java.classLoader),
			parcel.readParcelable(LastApplyMessageUiModel::class.java.classLoader),
			parcel.createStringArrayList()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeStringList(codes)
		parcel.writeTypedList(voucherOrders)
		parcel.writeParcelable(additionalInfo, flags)
		parcel.writeParcelable(message, flags)
		parcel.writeStringList(listRedPromos)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<LastApplyUiModel> {
		override fun createFromParcel(parcel: Parcel): LastApplyUiModel {
			return LastApplyUiModel(parcel)
		}

		override fun newArray(size: Int): Array<LastApplyUiModel?> {
			return arrayOfNulls(size)
		}
	}
}