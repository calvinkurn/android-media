package com.tokopedia.shop.review.shop.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by nisie on 8/24/17.
 */
class ImageAttachmentViewModel : Parcelable {
    var attachmentId: Int
        private set
    var description: String?
        private set
    var uriThumbnail: String?
        private set
    var uriLarge: String?
        private set

    constructor(attachmentId: Int, description: String?,
                uriThumbnail: String?, uriLarge: String?) {
        this.attachmentId = attachmentId
        this.description = description
        this.uriThumbnail = uriThumbnail
        this.uriLarge = uriLarge
    }

    protected constructor(`in`: Parcel) {
        attachmentId = `in`.readInt()
        description = `in`.readString()
        uriThumbnail = `in`.readString()
        uriLarge = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(attachmentId)
        dest.writeString(description)
        dest.writeString(uriThumbnail)
        dest.writeString(uriLarge)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ImageAttachmentViewModel?> = object : Parcelable.Creator<ImageAttachmentViewModel?> {
            override fun createFromParcel(`in`: Parcel): ImageAttachmentViewModel? {
                return ImageAttachmentViewModel(`in`)
            }

            override fun newArray(size: Int): Array<ImageAttachmentViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}