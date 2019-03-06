package com.tokopedia.affiliatecommon.data.pojo.checkaffiliate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by milhamj on 10/17/18.
 */
data class AffiliateCheckData (
    @SerializedName("affiliateCheck")
    @Expose
    var affiliateCheck: AffiliateCheckPojo? = null
){}
