package com.tokopedia.gallery.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.gallery.adapter.TypeFactory

data class ImageReviewItem(var reviewId: String? = null,
                           var formattedDate: String? = null,
                           var reviewerName: String? = null,
                           var imageUrlThumbnail: String? = null,
                           var imageUrlLarge: String? = null,
                           var rating: Int = NO_RATING_DATA) : Visitable<TypeFactory>, Parcelable {

    override fun type(typeFactory: TypeFactory): Int  = typeFactory.type(this)

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(reviewId)
        parcel.writeString(formattedDate)
        parcel.writeString(reviewerName)
        parcel.writeString(imageUrlThumbnail)
        parcel.writeString(imageUrlLarge)
        parcel.writeInt(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageReviewItem> {
        const val NO_RATING_DATA = -1

        override fun createFromParcel(parcel: Parcel): ImageReviewItem {
            return ImageReviewItem(parcel)
        }

        override fun newArray(size: Int): Array<ImageReviewItem?> {
            return arrayOfNulls(size)
        }
    }
}
