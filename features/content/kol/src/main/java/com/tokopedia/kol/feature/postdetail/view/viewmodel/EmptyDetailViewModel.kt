package com.tokopedia.kol.feature.postdetail.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactory

/**
 * @author by yfsx on 12/10/18.
 */
data class EmptyDetailViewModel(val title: String = "", val subtitle: String = "")
    : Visitable<KolPostDetailTypeFactory>, Parcelable {
    override fun type(typeFactory: KolPostDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

    constructor(source: Parcel) : this(
            source.readString() ?: "",
            source.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(subtitle)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<EmptyDetailViewModel> = object : Parcelable.Creator<EmptyDetailViewModel> {
            override fun createFromParcel(source: Parcel): EmptyDetailViewModel = EmptyDetailViewModel(source)
            override fun newArray(size: Int): Array<EmptyDetailViewModel?> = arrayOfNulls(size)
        }
    }
}
