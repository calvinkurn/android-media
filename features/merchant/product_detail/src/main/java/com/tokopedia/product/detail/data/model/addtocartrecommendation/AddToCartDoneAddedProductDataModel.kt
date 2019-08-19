package com.tokopedia.product.detail.data.model.addtocartrecommendation

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.detail.view.adapter.AddToCartDoneTypeFactory

data class AddToCartDoneAddedProductDataModel(
        val productId: String?,
        val productName: String?,
        val productImageUr: String?
) : Visitable<AddToCartDoneTypeFactory>, Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun type(typeFactory: AddToCartDoneTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(productImageUr)
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