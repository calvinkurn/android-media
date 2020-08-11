package com.tokopedia.atc_variant.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_variant.view.adapter.AddToCartVariantAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 30/11/18.
 */
@Parcelize
data class NoteViewModel(
        var note: String = "",
        var noteCharMax: Int = 144
) : Visitable<AddToCartVariantAdapterTypeFactory>, Parcelable {

    override fun type(typeFactory: AddToCartVariantAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}