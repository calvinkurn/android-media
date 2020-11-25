package com.tokopedia.checkout.data.model.response.shipment_address_form

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
