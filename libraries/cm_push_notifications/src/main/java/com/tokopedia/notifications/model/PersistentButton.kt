package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * @author lalit.singh
 */
open class PersistentButton() : Parcelable {

    @SerializedName(CMConstant.PayloadKeys.APP_LINK)
    var appLink: String? = null

    @SerializedName(CMConstant.PayloadKeys.TEXT)
    var text: String? = null

    @SerializedName(CMConstant.PayloadKeys.ICON)
    var icon: String? = null

    var isAppLogo: Boolean = false

    @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
    var element_id: String? = ""

    protected constructor(`in`: Parcel) : this() {
        appLink = `in`.readString()
        text = `in`.readString()
        icon = `in`.readString()
        isAppLogo = `in`.readByte().toInt() != 0
        element_id = `in`.readString().toString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(appLink)
        parcel.writeString(text)
        parcel.writeString(icon)
        parcel.writeByte((if (isAppLogo) 1 else 0).toByte())
        parcel.writeString (element_id)
    }

    companion object CREATOR : Parcelable.Creator<PersistentButton> {
        override fun createFromParcel(parcel: Parcel): PersistentButton {
            return PersistentButton(parcel)
        }

        override fun newArray(size: Int): Array<PersistentButton?> {
            return arrayOfNulls(size)
        }
    }
}
