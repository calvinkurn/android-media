package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ShopListTypeFactory
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class ShopHeaderViewModel(
        val cpmModel: CpmModel = CpmModel(),
        val totalShopCount: Int = 0
): Parcelable, Visitable<ShopListTypeFactory> {

    override fun type(typeFactory: ShopListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(CpmModel::class.java.classLoader) ?: CpmModel(),
            parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(cpmModel, flags)
        parcel.writeInt(totalShopCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopHeaderViewModel> {
        override fun createFromParcel(parcel: Parcel): ShopHeaderViewModel {
            return ShopHeaderViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ShopHeaderViewModel?> {
            return arrayOfNulls(size)
        }
    }
}