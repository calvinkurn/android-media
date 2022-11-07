package com.tokopedia.kolcommon.data.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 12/07/18.
 */
data class FollowKol(
    @SerializedName("data")
    val data: FollowKolData = FollowKolData(),

    @SerializedName("error")
    val error: String = ""

) {

    data class FollowKolData(
        @SerializedName("status")
        val status: Int = 0,
    )
}