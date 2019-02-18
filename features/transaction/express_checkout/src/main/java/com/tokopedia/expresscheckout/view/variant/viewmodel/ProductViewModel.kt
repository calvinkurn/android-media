package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class ProductViewModel(
        var parentId: Int,
        var productName: String,
        var productPrice: Int,
        var productImageUrl: String,
        var productChildrenList: ArrayList<ProductChild>,
        var selectedVariantOptionsIdMap: LinkedHashMap<Int, Int> = LinkedHashMap(),
        var maxOrderQuantity: Int,
        var minOrderQuantity: Int
) : Visitable<CheckoutVariantAdapterTypeFactory>, Parcelable {

    constructor(parcel: Parcel? = null) : this(
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            arrayListOf<ProductChild>().apply {
                parcel?.readList(this, ProductChild::class.java.classLoader)
            },
            linkedMapOf<Int, Int>().apply {
                parcel?.readMap(this, Int::class.java.classLoader)
            },
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0
    )

    override fun type(typeFactory: CheckoutVariantAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(parentId)
        parcel.writeString(productName)
        parcel.writeInt(productPrice)
        parcel.writeString(productImageUrl)
        parcel.writeList(productChildrenList)
        parcel.writeMap(selectedVariantOptionsIdMap)
        parcel.writeInt(maxOrderQuantity)
        parcel.writeInt(minOrderQuantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductViewModel> {
        override fun createFromParcel(parcel: Parcel): ProductViewModel {
            return ProductViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductViewModel?> {
            return arrayOfNulls(size)
        }
    }

}