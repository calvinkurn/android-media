package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class NoteViewModel(
        var note: String,
        var noteCharMax: Int = 144
) : Visitable<CheckoutVariantAdapterTypeFactory>, Parcelable {

    constructor(parcel: Parcel? = null) : this(
            parcel?.readString() ?: "",
            parcel?.readInt() ?: 0)

    override fun type(typeFactory: CheckoutVariantAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(note)
        parcel.writeInt(noteCharMax)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoteViewModel> {
        override fun createFromParcel(parcel: Parcel): NoteViewModel {
            return NoteViewModel(parcel)
        }

        override fun newArray(size: Int): Array<NoteViewModel?> {
            return arrayOfNulls(size)
        }
    }

}