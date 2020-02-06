package com.tokopedia.saldodetails.response.model.saldoholdinfo.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SaldoHoldDepositHistory(

	@SerializedName("seller_data")
	val sellerData: List<SellerDataItem?>? = null,

	@SerializedName("total")
	val total: Int? = null,

	@SerializedName("code")
	val code: Int? = null,

	@SerializedName("ticker_message_isshow")
	val tickerMessageIsshow: Boolean? = null,

	@SerializedName("ticker_message_id")
	val tickerMessageId: String? = null,

	@SerializedName("ticker_message_en")
	val tickerMessageEn: String? = null,

	@SerializedName("error")
	val error: Boolean? = null,

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("total_fmt")
	val totalFmt: String? = null,

	@SerializedName("buyer_data")
	val buyerData: List<BuyerDataItem?>? = null
)/*:Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readArrayList(SellerDataItem::class.java.classLoader) as ArrayList<SellerDataItem>,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.readString(),
			parcel.readString(),
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.readString(),
			parcel.readString(),
			parcel.readArrayList(BuyerDataItem::class.java.classLoader) as ArrayList<BuyerDataItem>
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeList(sellerData)
		parcel.writeValue(total)
		parcel.writeValue(code)
		parcel.writeValue(tickerMessageIsshow)
		parcel.writeString(tickerMessageId)
		parcel.writeString(tickerMessageEn)
		parcel.writeValue(error)
		parcel.writeString(message)
		parcel.writeString(totalFmt)
		parcel.writeList(buyerData)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<SaldoHoldDepositHistory> {
		override fun createFromParcel(parcel: Parcel): SaldoHoldDepositHistory {
			return SaldoHoldDepositHistory(parcel)
		}

		override fun newArray(size: Int): Array<SaldoHoldDepositHistory?> {
			return arrayOfNulls(size)
		}
	}
}
*/