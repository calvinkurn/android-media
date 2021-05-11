package com.tokopedia.minicart.common.data.response.minicartlistsimplified

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("available_section")
        val availableSection: AvailableSection = AvailableSection(),
        @SerializedName("unavailable_section")
        val unavailableSection: List<UnavailableSection> = emptyList()
)