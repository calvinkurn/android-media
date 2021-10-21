package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Nisie on 2/16/16.
 */
class ImageUpload : Parcelable {
    var imageId: String = "
    var fileLoc: String = ""
    var picSrc: String = ""
    var picSrcLarge: String = ""
    var picObj: String = ""
    var position: Int = 0
    var description: String = ""
    var isSelected: Boolean = false

    constructor()
    constructor(picSrc: String, picSrcLarge: String, description: String, imageId: String) {
        this.picSrcLarge = picSrcLarge
        this.picSrc = picSrc
        this.description = description
        this.imageId = imageId
    }

    protected constructor(`in`: Parcel) {
        imageId = `in`.readString().toString()
        fileLoc = `in`.readString().toString()
        picSrc = `in`.readString().toString()
        picSrcLarge = `in`.readString().toString()
        picObj = `in`.readString().toString()
        position = `in`.readInt()
        description = `in`.readString().toString()
        isSelected = `in`.readByte().toInt() != 0
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
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
        @JvmField
        val CREATOR: Parcelable.Creator<ImageUpload?> = object : Parcelable.Creator<ImageUpload?> {
            override fun createFromParcel(`in`: Parcel): ImageUpload? {
                return ImageUpload(`in`)
            }

            override fun newArray(size: Int): Array<ImageUpload?> {
                return arrayOfNulls(size)
            }
        }
    }
}