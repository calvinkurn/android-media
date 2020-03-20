package com.tokopedia.purchase_platform.features.promo.data.request.validate_use

import android.os.Parcel
import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class OrdersItem(

		@field:SerializedName("shipping_id")
        var shippingId: Int = -1,

		@field:SerializedName("shop_id")
        var shopId: Int = -1,

		@field:SerializedName("codes")
        var codes: MutableList<String> = mutableListOf(),

		@field:SerializedName("unique_id")
        var uniqueId: String = "",

		@field:SerializedName("sp_id")
        var spId: Int = -1,

		@field:SerializedName("product_details")
        var productDetails: List<ProductDetailsItem?> = listOf(),

		@SerializedName("is_checked")
        var isChecked: Boolean = false
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readInt(),
			parcel.readInt(),
			parcel.createStringArrayList() ?: mutableListOf(),
			parcel.readString() ?: "",
			parcel.readInt(),
			parcel.createTypedArrayList(ProductDetailsItem) ?: emptyList(),
			parcel.readByte() != 0.toByte()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(shippingId)
		parcel.writeInt(shopId)
		parcel.writeStringList(codes)
		parcel.writeString(uniqueId)
		parcel.writeInt(spId)
		parcel.writeTypedList(productDetails)
		parcel.writeByte(if (isChecked) 1 else 0)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<OrdersItem> {
		override fun createFromParcel(parcel: Parcel): OrdersItem {
			return OrdersItem(parcel)
		}

		override fun newArray(size: Int): Array<OrdersItem?> {
			return arrayOfNulls(size)
		}
	}

}