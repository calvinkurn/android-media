package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.filter

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by nisie on 8/21/17.
 */
open class OptionUiModel : Parcelable {
    var name: String?
    var key: String?
    var value: String?
    var isSelected: Boolean
    var isActive: Boolean
    var position: Int

    constructor(name: String?, key: String?, value: String?, position: Int) {
        this.name = name
        this.key = key
        this.value = value
        isSelected = false
        isActive = false
        this.position = position
    }

    constructor(name: String?) {
        this.name = name
        key = ""
        value = ""
        isSelected = false
        isActive = false
        position = 0
    }

    protected constructor(`in`: Parcel) {
        name = `in`.readString()
        key = `in`.readString()
        value = `in`.readString()
        isSelected = `in`.readByte().toInt() != 0
        isActive = `in`.readByte().toInt() != 0
        position = `in`.readInt()
    }

    public override fun describeContents(): Int {
        return 0
    }

    public override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(key)
        dest.writeString(value)
        dest.writeByte((if (isSelected) 1 else 0).toByte())
        dest.writeByte((if (isActive) 1 else 0).toByte())
        dest.writeInt(position)
    }

    companion object {
        val CREATOR: Parcelable.Creator<OptionUiModel> =
            object : Parcelable.Creator<OptionUiModel?> {
                public override fun createFromParcel(`in`: Parcel): OptionUiModel? {
                    return OptionUiModel(`in`)
                }

                public override fun newArray(size: Int): Array<OptionUiModel?> {
                    return arrayOfNulls(size)
                }
            }
    }
}