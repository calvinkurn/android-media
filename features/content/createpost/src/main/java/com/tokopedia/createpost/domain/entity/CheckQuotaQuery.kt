package com.tokopedia.createpost.domain.entity

import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 12/10/18.
 */
data class CheckQuotaQuery(
    @SerializedName("affiliatePostQuota")
    val data: CheckQuotaPojo = CheckQuotaPojo()
) {
    class CheckQuotaPojo {
        @SerializedName("number")
        val number = 0
    }
}
