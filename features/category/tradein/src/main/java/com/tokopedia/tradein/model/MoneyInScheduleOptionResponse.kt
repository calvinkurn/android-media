package com.tokopedia.tradein.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoneyInScheduleOptionResponse(
        val data: ResponseData?
) : Parcelable {
    @Parcelize
    data class ResponseData(
            val getPickupScheduleOption: GetPickupScheduleOption
    ) : Parcelable {
        @Parcelize
        data class GetPickupScheduleOption(
                @SerializedName("ScheduleDate")
                val scheduleDate: ArrayList<ScheduleDate>
        ) : Parcelable {
            @Parcelize
            data class ScheduleDate(
                    @SerializedName("DateFmt")
                    val dateFmt: String,
                    @SerializedName("ScheduleTime")
                    val scheduleTime: ArrayList<ScheduleTime>
            ) : Parcelable {
                @Parcelize
                data class ScheduleTime(
                        @SerializedName("MaxTimeUnix")
                        val maxTimeUnix: Int,
                        @SerializedName("MinTimeUnix")
                        val minTimeUnix: Int,
                        @SerializedName("TimeFmt")
                        val timeFmt: String
                ) : Parcelable
            }
        }
    }
}