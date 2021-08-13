package com.tokopedia.product.detail.data.model.addtocartrecommendation

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.detail.view.adapter.AddToCartDoneTypeFactory

data class AddToCartDoneAddedProductDataModel(
        val productId: String?,
        val productName: String?,
        val productImageUr: String?,
        val isVariant: Boolean?,
        val shopId: Int,
        val bebasOngkirUrl: String?,
) : Visitable<AddToCartDoneTypeFactory>, Parcelable {

    override fun type(typeFactory: AddToCartDoneTypeFactory): Int {
        return typeFactory.type(this)
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readInt(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(productImageUr)
        parcel.writeValue(isVariant)
        parcel.writeInt(shopId)
        parcel.writeString(bebasOngkirUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddToCartDoneAddedProductDataModel> {
        override fun createFromParcel(parcel: Parcel): AddToCartDoneAddedProductDataModel {
            return AddToCartDoneAddedProductDataModel(parcel)
        }

        override fun newArray(size: Int): Array<AddToCartDoneAddedProductDataModel?> {
            return arrayOfNulls(size)
        }
    }
}