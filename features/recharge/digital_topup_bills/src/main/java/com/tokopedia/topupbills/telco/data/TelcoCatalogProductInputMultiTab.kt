package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType

/**
 * Created by nabillasabbaha on 10/05/19.
 */
data class TelcoCatalogProductInputMultiTab(
        @SerializedName("rechargeCatalogProductInputMultiTab")
        @Expose
        val rechargeCatalogProductDataData: TelcoCatalogProductInputData = TelcoCatalogProductInputData())