package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 27/05/19.
 */
data class TopupBillsEnquiryData (
    @SerializedName("rechargeInquiry")
    @Expose
    val enquiry: TopupBillsEnquiry
)