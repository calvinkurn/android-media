package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class AccountLinkingResponse(
    @SerializedName("accountsLinkerStatus")
    val accountsLinkerStatus: AccountsLinkerStatus = AccountsLinkerStatus()
)

data class LinkStatusItem(
    @SerializedName("linking_type")
    val linkingType: String = "",

    @SerializedName("linked_time")
    val linkedTime: String = "",

    @SerializedName("partner_user_id")
    val partnerUserId: String = "",

    @SerializedName("status")
    val status: String = ""
)

data class AccountsLinkerStatus(
    @SerializedName("link_status")
    val linkStatus: List<LinkStatusItem> = emptyList(),

    @SerializedName("error")
    val error: String = ""
)
