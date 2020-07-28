package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 16/08/18.
 */

data class OfficialStore(
    @SerializedName("is_official")
    @Expose
    val isOfficial: Int = 0,
    @SerializedName("os_logo_url")
    @Expose
    val osLogoUrl: String = ""
)
