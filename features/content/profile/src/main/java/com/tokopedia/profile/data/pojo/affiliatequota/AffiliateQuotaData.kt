package com.tokopedia.profile.data.pojo.affiliatequota

import com.google.gson.annotations.SerializedName

data class AffiliateQuotaData(
        @SerializedName("affiliatePostQuota")
        val affiliatePostQuota: AffiliatePostQuota = AffiliatePostQuota()
)