package com.tokopedia.notifications.model

import androidx.room.ColumnInfo
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * @author lalit.singh
 */
open class PersistentButton() : Parcelable {

    @SerializedName(CMConstant.PayloadKeys.APP_LINK)
    @ColumnInfo(name = CMConstant.PayloadKeys.APP_LINK)
    @Expose
    var appLink: String? = null

    @SerializedName(CMConstant.PayloadKeys.TEXT)
    @ColumnInfo(name = CMConstant.PayloadKeys.TEXT)
    @Expose
    var text: String? = null

    @SerializedName(CMConstant.PayloadKeys.ICON)
    @ColumnInfo(name = CMConstant.PayloadKeys.ICON)
    @Expose
    var icon: String? = null

    @SerializedName("isAppLogo")
    @ColumnInfo(name = "isAppLogo")
    @Expose
    var isAppLogo: Boolean = false

    @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
    @Expose
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
