package com.tokopedia.affiliate.feature.createpost.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by milhamj on 21/02/19.
 */
data class RelatedProductItem(
        val id: String = "",
        val name: String = "",
        val price: String = "",
        val image: String = "",
        val type: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(price)
        writeString(image)
        writeString(type)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RelatedProductItem> = object : Parcelable.Creator<RelatedProductItem> {
            override fun createFromParcel(source: Parcel): RelatedProductItem = RelatedProductItem(source)
            override fun newArray(size: Int): Array<RelatedProductItem?> = arrayOfNulls(size)
        }
    }
}