package com.tokopedia.feedcomponent.view.viewmodel.data.template

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by yfsx on 23/03/19.
 */
data class TemplateFooterViewModel(
        val like: Boolean = false,
        val comment: Boolean = false,
        val share: Boolean = false,
        val ctaLink: Boolean = false
) : Parcelable {
    constructor(source: Parcel) : this(
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (like) 1 else 0))
        writeInt((if (comment) 1 else 0))
        writeInt((if (share) 1 else 0))
        writeInt((if (ctaLink) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TemplateFooterViewModel> = object : Parcelable.Creator<TemplateFooterViewModel> {
            override fun createFromParcel(source: Parcel): TemplateFooterViewModel = TemplateFooterViewModel(source)
            override fun newArray(size: Int): Array<TemplateFooterViewModel?> = arrayOfNulls(size)
        }
    }
}