package com.tokopedia.digital.newcart.presentation.model.cart

import android.os.Parcel
import android.os.Parcelable

class CrossSellingConfig() : Parcelable {
    var isSkipAble: Boolean = false
    var isChecked: Boolean = false
    var headerTitle: String? = null
    var bodyTitle: String? = null
    var bodyContentBefore: String? = null
    var bodyContentAfter: String? = null
    var checkoutButtonText: String? = null

    protected constructor(`in`: Parcel): this() {
        isSkipAble = `in`.readByte().toInt() != 0
        isChecked = `in`.readByte().toInt() != 0
        headerTitle = `in`.readString()
        bodyTitle = `in`.readString()
        bodyContentBefore = `in`.readString()
        bodyContentAfter = `in`.readString()
        checkoutButtonText = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte((if (isSkipAble) 1 else 0).toByte())
        dest.writeByte((if (isChecked) 1 else 0).toByte())
        dest.writeString(headerTitle)
        dest.writeString(bodyTitle)
        dest.writeString(bodyContentBefore)
        dest.writeString(bodyContentAfter)
        dest.writeString(checkoutButtonText)
    }

    companion object CREATOR : Parcelable.Creator<CrossSellingConfig> {
        override fun createFromParcel(`in`: Parcel): CrossSellingConfig {
            return CrossSellingConfig(`in`)
        }

        override fun newArray(size: Int): Array<CrossSellingConfig?> {
            return arrayOfNulls(size)
        }
    }
}
