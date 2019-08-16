package com.tokopedia.tradein.model

import com.google.gson.annotations.SerializedName

data class MoneyInScheduleOptionResponse(
        val data: Data?
) {
    data class Data(
            val getPickupScheduleOption: GetPickupScheduleOption?
    ) {
        data class GetPickupScheduleOption(
                @SerializedName("ScheduleDate")
                val scheduleDate: List<ScheduleDate?>?
        ) {
            data class ScheduleDate(
                    @SerializedName("DateFmt")
                    val dateFmt: String?,
                    @SerializedName("ScheduleTime")
                    val scheduleTime: List<ScheduleTime?>?
            ) {
                data class ScheduleTime(
                        @SerializedName("MaxTimeUnix")
                        val maxTimeUnix: Int?,
                        @SerializedName("MinTimeUnix")
                        val minTimeUnix: Int?,
                        @SerializedName("TimeFmt")
                        val timeFmt: String?
                )
            }
        }
    }
}