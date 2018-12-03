package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypefactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class CheckoutVariantNoteViewModel(
        var note: String,
        var noteCharMax: Int
) : Visitable<CheckoutVariantAdapterTypefactory>, Parcelable {

    constructor(parcel: Parcel? = null) : this(
            parcel?.readString() ?: "",
            parcel?.readInt() ?: 0)

    override fun type(typeFactory: CheckoutVariantAdapterTypefactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(note)
        parcel.writeInt(noteCharMax)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckoutVariantNoteViewModel> {
        override fun createFromParcel(parcel: Parcel): CheckoutVariantNoteViewModel {
            return CheckoutVariantNoteViewModel(parcel)
        }

        override fun newArray(size: Int): Array<CheckoutVariantNoteViewModel?> {
            return arrayOfNulls(size)
        }
    }

}