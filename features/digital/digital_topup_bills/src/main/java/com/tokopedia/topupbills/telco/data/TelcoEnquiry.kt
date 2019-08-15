package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 27/05/19.
 */
class TelcoEnquiry (
    @SerializedName("attributes")
    @Expose
    val attributes: TelcoEnquiryAttribute
)