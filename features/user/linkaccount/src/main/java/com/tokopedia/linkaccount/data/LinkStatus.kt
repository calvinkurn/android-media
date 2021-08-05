package com.tokopedia.linkaccount.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris on 05/08/21.
 */

data class LinkStatusResponse(
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("link_status")
    val linkStatus: ArrayList<LinkStatus> = arrayListOf()
)

data class LinkStatus(
    @SerializedName("linking_type")
    val type: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("linked_date")
    val linkDate: String = ""
)