package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcel
import android.os.Parcelable

data class LastApplyShopGroupSimplifiedData(
		val codes: List<String> = emptyList(),
		val voucherOrders: List<VoucherOrdersItem> = emptyList(),
		val additionalInfo: AdditionalInfo = AdditionalInfo(),
		val message: Message = Message(),
		var listRedPromos: List<String> = emptyList()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.createStringArrayList(),
			parcel.createTypedArrayList(VoucherOrdersItem),
			parcel.readParcelable(AdditionalInfo::class.java.classLoader),
			parcel.readParcelable(Message::class.java.classLoader),
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

	companion object CREATOR : Parcelable.Creator<LastApplyShopGroupSimplifiedData> {
		override fun createFromParcel(parcel: Parcel): LastApplyShopGroupSimplifiedData {
			return LastApplyShopGroupSimplifiedData(parcel)
		}

		override fun newArray(size: Int): Array<LastApplyShopGroupSimplifiedData?> {
			return arrayOfNulls(size)
		}
	}
}
