package com.tokopedia.kyc_centralized.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by alvinatin on 21/11/18.
 */
class ImageUploadModel : Parcelable {
    var kycType: Int
    var picObjKyc: String? = null
    var filePath: String?
    var fileName: String? = null
    var isSuccess = 0
    var error: String? = null

    constructor(kycType: Int, filePath: String?) {
        this.kycType = kycType
        this.filePath = filePath
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(kycType)
        dest.writeString(picObjKyc)
        dest.writeString(filePath)
        dest.writeString(fileName)
        dest.writeInt(isSuccess)
        dest.writeString(error)
    }

    constructor(`in`: Parcel) {
        kycType = `in`.readInt()
        picObjKyc = `in`.readString()
        filePath = `in`.readString()
        fileName = `in`.readString()
        isSuccess = `in`.readInt()
        error = `in`.readString()
    }

    companion object {
        val CREATOR: Parcelable.Creator<ImageUploadModel?> = object : Parcelable.Creator<ImageUploadModel?> {
            override fun createFromParcel(source: Parcel): ImageUploadModel? {
                return ImageUploadModel(source)
            }

            override fun newArray(size: Int): Array<ImageUploadModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}