package com.tokopedia.notifications.model

import androidx.room.ColumnInfo
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * Created by Ashwani Tyagi on 22/10/18.
 */
data class ActionButton(
        @SerializedName(CMConstant.PayloadKeys.TEXT)
        @ColumnInfo(name = CMConstant.PayloadKeys.TEXT)
        @Expose
        var text: String? = null,

        @SerializedName(CMConstant.PayloadKeys.APP_LINK)
        @ColumnInfo(name = CMConstant.PayloadKeys.APP_LINK)
        @Expose
        var appLink: String? = null,

        @SerializedName(CMConstant.PayloadKeys.ACTION_BUTTON_ICON)
        @ColumnInfo(name = CMConstant.PayloadKeys.ACTION_BUTTON_ICON)
        @Expose
        var actionButtonIcon: String? = null,

        @SerializedName(CMConstant.PayloadKeys.PD_ACTION)
        @ColumnInfo(name = CMConstant.PayloadKeys.PD_ACTION)
        @Expose
        var pdActions: PreDefineActions? = null,

        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        @ColumnInfo(name = CMConstant.PayloadKeys.ELEMENT_ID)
        @Expose
        var element_id: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(PreDefineActions::class.java.classLoader),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(appLink)
        parcel.writeString(actionButtonIcon)
        parcel.writeParcelable(pdActions, flags)
        parcel.writeString(element_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActionButton> {
        override fun createFromParcel(parcel: Parcel): ActionButton {
            return ActionButton(parcel)
        }

        override fun newArray(size: Int): Array<ActionButton?> {
            return arrayOfNulls(size)
        }
    }

}
