package com.tokopedia.feedcomponent.data.pojo.whitelist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 20/06/18.
 */

data class WhitelistQuery (
    @SerializedName("feed_check_whitelist")
    @Expose
    val whitelist: Whitelist = Whitelist()
)
