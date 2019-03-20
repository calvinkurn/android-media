package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import android.os.Parcel
import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class VoucherOrdersItem(

	@field:SerializedName("cart_id")
	val cartId: Int? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("unique_id")
	val uniqueId: String? = null,

	@field:SerializedName("cashback_wallet_amount")
	val cashbackWalletAmount: Int? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("discount_amount")
	val discountAmount: Int? = null,

	@field:SerializedName("invoice_description")
	val invoiceDescription: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("message")
	val message: Message? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString(),
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString(),
			parcel.readString(),
			parcel.readParcelable(Message::class.java.classLoader))

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(cartId)
		parcel.writeString(code)
		parcel.writeString(uniqueId)
		parcel.writeValue(cashbackWalletAmount)
		parcel.writeValue(success)
		parcel.writeValue(discountAmount)
		parcel.writeString(invoiceDescription)
		parcel.writeString(type)
		parcel.writeParcelable(message, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<VoucherOrdersItem> {
		override fun createFromParcel(parcel: Parcel): VoucherOrdersItem {
			return VoucherOrdersItem(parcel)
		}

		override fun newArray(size: Int): Array<VoucherOrdersItem?> {
			return arrayOfNulls(size)
		}
	}
}