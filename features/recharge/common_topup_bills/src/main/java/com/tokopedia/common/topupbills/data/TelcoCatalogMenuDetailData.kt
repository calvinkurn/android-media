package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 28/05/19.
 */
class TelcoCatalogMenuDetailData(
        @SerializedName("rechargeCatalogMenuDetail")
        @Expose
        val catalogMenuDetailData: TopupBillsMenuDetail
)