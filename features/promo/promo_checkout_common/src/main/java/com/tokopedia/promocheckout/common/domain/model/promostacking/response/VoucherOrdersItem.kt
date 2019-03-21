package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import android.os.Parcel
import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class VoucherOrdersItem(

	@field:SerializedName("cart_id")
	val cartId: Int? = 0,

	@field:SerializedName("code")
	val code: String? = "",

	@field:SerializedName("unique_id")
	val uniqueId: String? = "",

	@field:SerializedName("cashback_wallet_amount")
	val cashbackWalletAmount: Int? = 0,

	@field:SerializedName("success")
	val success: Boolean? = false,

	@field:SerializedName("discount_amount")
	val discountAmount: Int? = 0,

	@field:SerializedName("invoice_description")
	val invoiceDescription: String? = "",

	@field:SerializedName("type")
	val type: String? = "",

	@field:SerializedName("message")
	val message: Message? = Message()
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