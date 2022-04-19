package com.tokopedia.additional_check.data

import com.google.gson.annotations.SerializedName


data class ShowInterruptResponse(
    @SerializedName("show_interrupt")
    val data: ShowInterruptData = ShowInterruptData()
)

data class ShowInterruptData(
    @SerializedName("interval")
    var interval: Int = 0,
    @SerializedName("show_skip")
    var showSkipButton: Boolean = false,
    @SerializedName("popup_2fa")
    var popupType: Int = 0,
    @SerializedName("error")
    var error: String = "",
    @SerializedName("account_link_reminder")
    var accountLinkReminderData: AccountLinkReminderData = AccountLinkReminderData()
)

data class AccountLinkReminderData(
    @SerializedName("interval")
    var interval: Int = 0,
    @SerializedName("show_reminder")
    var showReminder: Boolean = false
)