package com.tokopedia.affiliatecommon.data.pojo.checkaffiliate

import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 12/10/18.
 */
data class AffiliateCheckPojo (

    /**
     * isAffiliate : false
     * status : Not Affiliate
     */

    @SerializedName("isAffiliate")
    var isIsAffiliate: Boolean = false,

    @SerializedName("status")
    var status: String = ""
)
