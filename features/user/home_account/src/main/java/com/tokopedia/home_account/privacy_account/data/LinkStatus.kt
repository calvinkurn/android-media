package com.tokopedia.home_account.privacy_account.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris on 05/08/21.
 */

@Deprecated("Remove this class after integrating SCP Login to Tokopedia")
data class LinkStatusResponse(
    @SerializedName("accountsLinkerStatus")
    val response: LinkStatusPojo = LinkStatusPojo()
)
@Deprecated("Remove this class after integrating SCP Login to Tokopedia")
data class LinkStatusPojo(
    @SerializedName("link_status")
    val linkStatus: ArrayList<LinkStatus> = arrayListOf(),
    @SerializedName("error")
    val error: String = ""
)
@Deprecated("Remove this class after integrating SCP Login to Tokopedia")
data class LinkStatus(
    @SerializedName("linking_type")
    val type: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("partner_user_id")
    val partnerId: String = "",
    @SerializedName("linked_time")
    val linkedDate: String = "",

    @Transient
    var phoneNo: String = ""
)
