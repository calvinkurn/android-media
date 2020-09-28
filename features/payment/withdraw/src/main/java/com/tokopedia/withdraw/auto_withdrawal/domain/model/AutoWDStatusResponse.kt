package com.tokopedia.withdraw.auto_withdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AutoWDStatusResponse(
        @SerializedName("GetAutoWDStatus")
        val autoWDStatus: AutoWDStatus
)

data class AutoWDStatus(
        @SerializedName("code") val code: Int = 0,
        @SerializedName("data") val autoWDStatusData: AutoWDStatusData,
        @SerializedName("message") val message: String = ""
)

data class AutoWDStatusData(
        @SerializedName("auto_wd_user_id") val auto_wd_user_id: Int = 0,
        @SerializedName("is_owner") var isOwner: Boolean = false,
        @SerializedName("is_power_wd") var isPowerWd: Boolean = false,
        @SerializedName("schedule") val scheduleList: ArrayList<Schedule>,
        @SerializedName("status") var status: Int = -1,
        @SerializedName("user_id") val user_id: Int = 0
)

data class Schedule(
        @SerializedName("auto_wd_schedule_id") val autoWithdrawalScheduleId: Int = 0,
        @SerializedName("desc") val desc: String = "",
        @SerializedName("scheduleType") val scheduleType: Int = 0,
        @SerializedName("status") var status: Int = 0,
        @SerializedName("title") val title: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(autoWithdrawalScheduleId)
        parcel.writeString(desc)
        parcel.writeInt(scheduleType)
        parcel.writeInt(status)
        parcel.writeString(title)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Schedule && other.title == title
                && other.desc == desc && other.scheduleType == scheduleType) {
            return true
        }
        return super.equals(other)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Schedule> {
        override fun createFromParcel(parcel: Parcel): Schedule {
            return Schedule(parcel)
        }

        override fun newArray(size: Int): Array<Schedule?> {
            return arrayOfNulls(size)
        }
    }
}