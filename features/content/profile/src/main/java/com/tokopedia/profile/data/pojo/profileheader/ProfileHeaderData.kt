package com.tokopedia.profile.data.pojo.profileheader

import com.google.gson.annotations.SerializedName

data class ProfileHeaderData(
        @SerializedName("affiliatePostQuota")
        val affiliatePostQuota: AffiliatePostQuota = AffiliatePostQuota(),

        @SerializedName("bymeProfileHeader")
        val bymeProfileHeader: BymeProfileHeader = BymeProfileHeader()
)