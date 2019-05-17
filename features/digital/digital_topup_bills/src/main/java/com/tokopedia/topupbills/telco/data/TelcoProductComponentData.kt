package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class TelcoProductComponentData(
        @SerializedName("rechargeComponentDataCollection")
        @Expose
        val rechargeProductData: TelcoProductData,
        var productType: Int = TelcoProductType.PRODUCT_GRID)