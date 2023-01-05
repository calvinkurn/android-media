package com.tokopedia.kol.feature.comment.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by nisie on 11/2/17.
 */

data class KolCommentProductUiModel(
    val imageUrl: String,
    val name: String,
    val price: String,
    private val isWishlisted: Boolean
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(imageUrl)
        dest.writeString(name)
        dest.writeString(price)
        dest.writeByte((if (isWishlisted) 1 else 0))
    }

    companion object CREATOR : Parcelable.Creator<KolCommentProductUiModel> {
        override fun createFromParcel(parcel: Parcel): KolCommentProductUiModel {
            return KolCommentProductUiModel(parcel)
        }

        override fun newArray(size: Int): Array<KolCommentProductUiModel?> {
            return arrayOfNulls(size)
        }
    }
}
