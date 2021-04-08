package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

data class OfficialStore(
        @SerializedName("is_official")
        val isOfficial: Int = 0,
        @SerializedName("os_logo_url")
        val osLogoUrl: String = ""
)
