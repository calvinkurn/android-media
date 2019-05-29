package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * Created by Ashwani Tyagi on 22/10/18.
 */
data class ActionButton (
        @SerializedName(CMConstant.PayloadKeys.TEXT)
        var text: String? = null,

        @SerializedName(CMConstant.PayloadKeys.APP_LINK)
        var appLink: String? = null,

        @SerializedName(CMConstant.PayloadKeys.ACTION_BUTTON_ICON)
        var actionButtonIcon: String? = null,

        @SerializedName(CMConstant.PayloadKeys.PD_ACTION)
        var pdActions: PreDefineActions? = null
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readParcelable(PreDefineActions::class.java.classLoader))

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(text)
                parcel.writeString(appLink)
                parcel.writeString(actionButtonIcon)
                parcel.writeParcelable(pdActions, flags)
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
