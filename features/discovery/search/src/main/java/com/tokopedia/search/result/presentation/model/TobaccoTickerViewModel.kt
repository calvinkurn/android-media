package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class TobaccoTickerViewModel(
        val htmlErrorMessage: String
): Parcelable, Visitable<ProductListTypeFactory> {

    constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(htmlErrorMessage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TobaccoTickerViewModel> {
        override fun createFromParcel(parcel: Parcel): TobaccoTickerViewModel {
            return TobaccoTickerViewModel(parcel)
        }

        override fun newArray(size: Int): Array<TobaccoTickerViewModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}