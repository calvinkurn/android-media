package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcel
import android.os.Parcelable


data class CampaignTimerUi(
        var dialogButton: String = "",
        var dialogDescription: String = "",
        var dialogTitle: String = "",
        var showTimer: Boolean = false,
        var timerDeduct: String = "",
        var timerDescription: String = "",
        var timerExpired: String = "",
        var timerExpiredDuration: Int = 0,
        var timerServer: String = "",
        var gtmProductId: Int = 0,
        var gtmUserId: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
    source.readString(),
    source.readString(),
    source.readString(),
    1 == source.readInt(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readInt(),
    source.readString(),
    source.readInt(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(dialogButton)
        writeString(dialogDescription)
        writeString(dialogTitle)
        writeInt((if (showTimer) 1 else 0))
        writeString(timerDeduct)
        writeString(timerDescription)
        writeString(timerExpired)
        writeInt(timerExpiredDuration)
        writeString(timerServer)
        writeInt(gtmProductId)
        writeString(gtmUserId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CampaignTimerUi> = object : Parcelable.Creator<CampaignTimerUi> {
            override fun createFromParcel(source: Parcel): CampaignTimerUi = CampaignTimerUi(source)
            override fun newArray(size: Int): Array<CampaignTimerUi?> = arrayOfNulls(size)
        }
    }
}