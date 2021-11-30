package com.tokopedia.sellerorder.list.domain.model

import com.google.gson.annotations.SerializedName

data class SomListGetMultiShippingParam(
        @SerializedName("job_id")
        val jobId: String = ""
)