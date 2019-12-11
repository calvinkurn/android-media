package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 27/05/19.
 */
class TopupBillsEnquiry (
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("retrySec")
    @Expose
    val retryDuration: Int?,
    @SerializedName("attributes")
    @Expose
    val attributes: TopupBillsEnquiryAttribute
)