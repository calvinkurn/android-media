package com.tokopedia.common.topupbills.data.prefix_select

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 10/05/19.
 */
@Parcelize
data class TelcoCatalogPrefixSelect(
        @SerializedName("rechargeCatalogPrefixSelect")
        @Expose
        val rechargeCatalogPrefixSelect: RechargeCatalogPrefixSelect = RechargeCatalogPrefixSelect()
): Parcelable