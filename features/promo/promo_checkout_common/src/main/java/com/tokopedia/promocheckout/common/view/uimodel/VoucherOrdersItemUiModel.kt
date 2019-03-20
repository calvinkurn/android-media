package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

data class VoucherOrdersItemUiModel(
		var success: Boolean = false,
		var code: String = "",
		var uniqueId: String = "",
		var cartId: Int = -1,
		var type: String = "",
		var cashbackWalletAmount: Int = -1,
		var discountAmount: Int = -1,
		var invoiceDescription: String = "",
		var message: MessageUiModel = MessageUiModel()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readByte() != 0.toByte(),
			parcel.readString(),
			parcel.readString(),
			parcel.readInt(),
			parcel.readString(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readString(),
			parcel.readParcelable(MessageUiModel::class.java.classLoader)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeByte(if (success) 1 else 0)
		parcel.writeString(code)
		parcel.writeString(uniqueId)
		parcel.writeInt(cartId)
		parcel.writeString(type)
		parcel.writeInt(cashbackWalletAmount)
		parcel.writeInt(discountAmount)
		parcel.writeString(invoiceDescription)
		parcel.writeParcelable(message, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<VoucherOrdersItemUiModel> {
		override fun createFromParcel(parcel: Parcel): VoucherOrdersItemUiModel {
			return VoucherOrdersItemUiModel(parcel)
		}

		override fun newArray(size: Int): Array<VoucherOrdersItemUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
