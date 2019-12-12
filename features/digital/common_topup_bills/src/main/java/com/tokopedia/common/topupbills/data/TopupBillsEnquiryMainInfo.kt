package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 27/05/19.
 */
class TopupBillsEnquiryMainInfo(
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("value")
        @Expose
        val value: String = ""
)