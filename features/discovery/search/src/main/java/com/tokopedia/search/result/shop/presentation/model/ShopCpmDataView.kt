package com.tokopedia.search.result.shop.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.shop.presentation.typefactory.ShopListTypeFactory
import com.tokopedia.topads.sdk.domain.model.CpmModel

internal data class ShopCpmDataView(
        val cpmModel: CpmModel = CpmModel()
): Parcelable, Visitable<ShopListTypeFactory> {

    override fun toString(): String {
        return this.javaClass.canonicalName ?: ""
    }

    override fun type(typeFactory: ShopListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    constructor(parcel: Parcel) : this(parcel.readParcelable(CpmModel::class.java.classLoader) ?: CpmModel())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(cpmModel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopCpmDataView> {
        override fun createFromParcel(parcel: Parcel): ShopCpmDataView {
            return ShopCpmDataView(parcel)
        }

        override fun newArray(size: Int): Array<ShopCpmDataView?> {
            return arrayOfNulls(size)
        }
    }
}