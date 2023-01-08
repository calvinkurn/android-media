package com.tokopedia.kolcommon.data.pojo

import com.google.gson.annotations.SerializedName
import com.tokopedia.kolcommon.data.pojo.FollowKol

/**
 * @author by yfsx on 12/07/18.
 */
data class FollowKolQuery(
    @SerializedName("do_follow_kol")
    val data: FollowKol = FollowKol()
)