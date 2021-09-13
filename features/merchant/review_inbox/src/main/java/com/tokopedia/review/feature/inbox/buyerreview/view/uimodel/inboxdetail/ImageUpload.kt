package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Nisie on 2/16/16.
 */
class ImageUpload : Parcelable {
    var imageId: String? = null
    var fileLoc: String? = null
    var picSrc: String? = null
    var picSrcLarge: String? = null
    var picObj: String? = null
    var position: Int = 0
    var description: String? = null
    var isSelected: Boolean = false

    constructor() {}
    constructor(picSrc: String?, picSrcLarge: String?, description: String?, imageId: String?) {
        this.picSrcLarge = picSrcLarge
        this.picSrc = picSrc
        this.description = description
        this.imageId = imageId
    }

    protected constructor(`in`: Parcel) {
        imageId = `in`.readString()
        fileLoc = `in`.readString()
        picSrc = `in`.readString()
        picSrcLarge = `in`.readString()
        picObj = `in`.readString()
        position = `in`.readInt()
        description = `in`.readString()
        isSelected = `in`.readByte().toInt() != 0
    }

    public override fun describeContents(): Int {
        return 0
    }

    public override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(imageId)
        dest.writeString(fileLoc)
        dest.writeString(picSrc)
        dest.writeString(picSrcLarge)
        dest.writeString(picObj)
        dest.writeInt(position)
        dest.writeString(description)
        dest.writeByte((if (isSelected) 1 else 0).toByte())
    }

    companion object {
        val CREATOR: Parcelable.Creator<ImageUpload> = object : Parcelable.Creator<ImageUpload?> {
            public override fun createFromParcel(`in`: Parcel): ImageUpload? {
                return ImageUpload(`in`)
            }

            public override fun newArray(size: Int): Array<ImageUpload?> {
                return arrayOfNulls(size)
            }
        }
    }
}