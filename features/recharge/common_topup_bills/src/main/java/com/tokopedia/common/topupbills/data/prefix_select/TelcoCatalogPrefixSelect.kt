package com.tokopedia.common.topupbills.data.prefix_select

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
data class TelcoCatalogPrefixSelect(
        @SerializedName("rechargeCatalogPrefixSelect")
        @Expose
        val rechargeCatalogPrefixSelect: RechargeCatalogPrefixSelect = RechargeCatalogPrefixSelect()
)