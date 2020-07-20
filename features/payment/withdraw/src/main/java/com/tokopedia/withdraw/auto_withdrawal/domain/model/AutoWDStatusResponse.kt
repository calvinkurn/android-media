package com.tokopedia.withdraw.auto_withdrawal.domain.model

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
        @SerializedName("is_owner") val isOwner: Boolean = false,
        @SerializedName("is_power_wd") val isPowerWd: Boolean = false,
        @SerializedName("schedule") val schedule: List<Schedule>,
        @SerializedName("status") val status: Int = -1,
        @SerializedName("user_id") val user_id: Int = 0
)

data class Schedule(
        @SerializedName("auto_wd_schedule_id") val autoWithdrawalScheduleId: Int = 0,
        @SerializedName("desc") val desc: String = "",
        @SerializedName("scheduleType") val scheduleType: Int = 0,
        @SerializedName("status") val status: Int = 0,
        @SerializedName("title") val title: String = ""
)