package com.tokopedia.gallery.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.gallery.adapter.TypeFactory

class ImageReviewItem : Visitable<TypeFactory>, Parcelable {

    var reviewId: String? = null
    var formattedDate: String? = null
    var reviewerName: String? = null
    var imageUrlThumbnail: String? = null
    var imageUrlLarge: String? = null
    var rating = NO_RATING_DATA

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.formattedDate)
        dest.writeString(this.reviewerName)
        dest.writeString(this.imageUrlThumbnail)
        dest.writeString(this.imageUrlLarge)
        dest.writeInt(this.rating)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.formattedDate = `in`.readString()
        this.reviewerName = `in`.readString()
        this.imageUrlThumbnail = `in`.readString()
        this.imageUrlLarge = `in`.readString()
        this.rating = `in`.readInt()
    }

    override fun type(typeFactory: TypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        val NO_RATING_DATA = -1

        val CREATOR: Parcelable.Creator<ImageReviewItem> = object : Parcelable.Creator<ImageReviewItem> {
            override fun createFromParcel(source: Parcel): ImageReviewItem {
                return ImageReviewItem(source)
            }

            override fun newArray(size: Int): Array<ImageReviewItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}
