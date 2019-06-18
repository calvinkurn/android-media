package com.tokopedia.affiliate.feature.explore.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactory

/**
 * @author by yfsx on 12/10/18.
 */
data class ExploreEmptySearchViewModel(val title: String = "", val subtitle: String = "")
    : Visitable<ExploreTypeFactory>, Parcelable {
    override fun type(typeFactory: ExploreTypeFactory): Int {
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
        val CREATOR: Parcelable.Creator<ExploreEmptySearchViewModel> = object : Parcelable.Creator<ExploreEmptySearchViewModel> {
            override fun createFromParcel(source: Parcel): ExploreEmptySearchViewModel = ExploreEmptySearchViewModel(source)
            override fun newArray(size: Int): Array<ExploreEmptySearchViewModel?> = arrayOfNulls(size)
        }
    }
}
