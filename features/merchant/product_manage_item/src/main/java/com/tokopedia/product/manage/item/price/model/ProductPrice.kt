package com.tokopedia.product.manage.item.price.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.product.manage.item.main.base.data.model.ProductWholesaleViewModel
import com.tokopedia.product.manage.item.utils.ProductEditCurrencyType
import java.util.ArrayList



data class ProductPrice(var price: Double = 0.0,
                        var currencyType: Int = ProductEditCurrencyType.RUPIAH,
                        var wholesalePrice: ArrayList<ProductWholesaleViewModel> = ArrayList(),
                        var minOrder: Int = 0,
                        var maxOrder: Int = 0): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readInt(),
            arrayListOf<ProductWholesaleViewModel>().apply {
                parcel.readList(this, ProductWholesaleViewModel::class.java.classLoader)
            },
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(price)
        parcel.writeInt(currencyType)
        parcel.writeList(wholesalePrice)
        parcel.writeInt(minOrder)
        parcel.writeInt(maxOrder)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductPrice> {
        override fun createFromParcel(parcel: Parcel): ProductPrice {
            return ProductPrice(parcel)
        }

        override fun newArray(size: Int): Array<ProductPrice?> {
            return arrayOfNulls(size)
        }
    }
}