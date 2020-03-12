package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcel
import android.os.Parcelable

data class AdditionalInfo(
		val messageInfo: MessageInfo = MessageInfo(),
		val errorDetail: ErrorDetail = ErrorDetail(),
		val emptyCartInfo: EmptyCartInfo = EmptyCartInfo()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readParcelable(MessageInfo::class.java.classLoader) ?: MessageInfo(),
			parcel.readParcelable(ErrorDetail::class.java.classLoader) ?: ErrorDetail(),
			parcel.readParcelable(EmptyCartInfo::class.java.classLoader) ?: EmptyCartInfo()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeParcelable(messageInfo, flags)
		parcel.writeParcelable(errorDetail, flags)
		parcel.writeParcelable(emptyCartInfo, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<AdditionalInfo> {
		override fun createFromParcel(parcel: Parcel): AdditionalInfo {
			return AdditionalInfo(parcel)
		}

		override fun newArray(size: Int): Array<AdditionalInfo?> {
			return arrayOfNulls(size)
		}
	}
}
