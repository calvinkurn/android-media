package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

data class DataUiModel(
		var globalSuccess: Boolean = false,
		var success: Boolean = false,
		var message: MessageUiModel = MessageUiModel(),
		var promoCodeId: Int = -1,
		var codes: List<String> = emptyList(),
		var titleDescription: String = "",
		var discountAmount: Int = -1,
		var cashbackWalletAmount: Int = -1,
		var cashbackAdvocateReferralAmount: Int = -1,
		var cashbackVoucherDescription: String = "",
		var invoiceDescription: String = "",
		var gatewayId: String = "",
		var isCoupon: Int = -1,
		var couponDescription: String = "",
		var clashings: ClashingInfoDetailUiModel = ClashingInfoDetailUiModel(),
		var voucherOrders: List<VoucherOrdersItemUiModel> = emptyList()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readByte() != 0.toByte(),
			parcel.readByte() != 0.toByte(),
			parcel.readParcelable(MessageUiModel::class.java.classLoader),
			parcel.readInt(),
			parcel.createStringArrayList(),
			parcel.readString(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readInt(),
			parcel.readString(),
			parcel.readParcelable(ClashingInfoDetailUiModel::class.java.classLoader),
			parcel.createTypedArrayList(VoucherOrdersItemUiModel)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeByte(if (globalSuccess) 1 else 0)
		parcel.writeByte(if (success) 1 else 0)
		parcel.writeParcelable(message, flags)
		parcel.writeInt(promoCodeId)
		parcel.writeStringList(codes)
		parcel.writeString(titleDescription)
		parcel.writeInt(discountAmount)
		parcel.writeInt(cashbackWalletAmount)
		parcel.writeInt(cashbackAdvocateReferralAmount)
		parcel.writeString(cashbackVoucherDescription)
		parcel.writeString(invoiceDescription)
		parcel.writeString(gatewayId)
		parcel.writeInt(isCoupon)
		parcel.writeString(couponDescription)
		parcel.writeParcelable(clashings, flags)
		parcel.writeTypedList(voucherOrders)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<DataUiModel> {
		override fun createFromParcel(parcel: Parcel): DataUiModel {
			return DataUiModel(parcel)
		}

		override fun newArray(size: Int): Array<DataUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
