package com.tokopedia.withdraw.auto_withdrawal.domain.model

import com.google.gson.annotations.SerializedName

data class AutoWDStatusResponse(
        @SerializedName("GetAutoWDStatus")
        val autoWDStatusData: AutoWDStatusData
)

data class AutoWDStatusData(
        @SerializedName("code") val code: Int = 0,
        @SerializedName("data") val data: Data?,
        @SerializedName("message") val message: String = ""
)

data class Data(
        @SerializedName("auto_wd_user_id") val auto_wd_user_id: Int = 0, //User ID specialized for Auto WD Feature
        @SerializedName("is_owner") val is_owner: Boolean = false, //admin or not
        @SerializedName("is_power_wd") val is_power_wd: Boolean = false, //rekening premium flag
        @SerializedName("schedule") val schedule: List<Schedule>?,
        @SerializedName("status") val status: Int = -1, //0 never opt in, 1 active, 2 inactive
        @SerializedName("user_id") val user_id: Int = 0
)

data class Schedule(
        @SerializedName("auto_wd_schedule_id") val auto_wd_schedule_id: Int = 0,
        @SerializedName("desc") val desc: String = "",
        @SerializedName("scheduleType") val scheduleType: Int = 0,
        @SerializedName("status") val status: Int = 0,
        @SerializedName("title") val title: String = ""
)