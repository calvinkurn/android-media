package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * @author lalit.singh
 */
data class Carousal(

        @SerializedName(CMConstant.PayloadKeys.APP_LINK)
        var appLink: String? = null,

        @SerializedName(CMConstant.PayloadKeys.TEXT)
        var text: String? = null,

        @SerializedName(CMConstant.PayloadKeys.IMG)
        var icon: String? = null,

        var filePath: String? = null,

        var index: Int = 0) : Parcelable {

    constructor(source: Parcel) : this(source.readString(), source.readString(),
            source.readString(), source.readString(), source.readInt())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.appLink)
        dest?.writeString(this.text)
        dest?.writeString(this.icon)
        dest?.writeString(this.filePath)
        dest?.writeInt(this.index)
    }


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Carousal> = object : Parcelable.Creator<Carousal> {
            override fun createFromParcel(source: Parcel): Carousal {
                return Carousal(source)
            }

            override fun newArray(size: Int): Array<Carousal?> {
                return arrayOfNulls(size)
            }
        }
    }

}