package com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse

import android.os.Parcel
import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ProductDetailsItem(

        @field:SerializedName("quantity")
        var quantity: Int = -1,

        @field:SerializedName("product_id")
        var productId: Int = -1
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readInt()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(quantity)
                parcel.writeInt(productId)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<ProductDetailsItem> {
                override fun createFromParcel(parcel: Parcel): ProductDetailsItem {
                        return ProductDetailsItem(parcel)
                }

                override fun newArray(size: Int): Array<ProductDetailsItem?> {
                        return arrayOfNulls(size)
                }
        }

}