package com.tokopedia.purchase_platform.features.promo.data.request.validate_use

import android.os.Parcel
import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ValidateUsePromoRequest(

        @field:SerializedName("codes")
        var codes: List<String?> = listOf(),

        @field:SerializedName("is_suggested")
        var isSuggested: Int = 1,

        @field:SerializedName("orders")
        var orders: List<OrdersItem?> = listOf(),

        @field:SerializedName("skip_apply")
        var skipApply: Int = 1,

        @field:SerializedName("cart_type")
        var cartType: String = "", // ocs & default

        @field:SerializedName("state")
        var state: String = "" // cart & checkout & occ
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.createStringArrayList() ?: emptyList(),
                parcel.readInt(),
                parcel.createTypedArrayList(OrdersItem) ?: emptyList(),
                parcel.readInt(),
                parcel.readString() ?: "",
                parcel.readString() ?: "") {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeStringList(codes)
                parcel.writeInt(isSuggested)
                parcel.writeTypedList(orders)
                parcel.writeInt(skipApply)
                parcel.writeString(cartType)
                parcel.writeString(state)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<ValidateUsePromoRequest> {
                override fun createFromParcel(parcel: Parcel): ValidateUsePromoRequest {
                        return ValidateUsePromoRequest(parcel)
                }

                override fun newArray(size: Int): Array<ValidateUsePromoRequest?> {
                        return arrayOfNulls(size)
                }
        }

}