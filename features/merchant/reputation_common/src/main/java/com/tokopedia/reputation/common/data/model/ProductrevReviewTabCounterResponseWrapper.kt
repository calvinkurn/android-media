package com.tokopedia.reputation.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevReviewTabCounterResponseWrapper(
        @SerializedName("productrevReviewTabCounter")
        @Expose
        val productrevReviewTabCounterList: ProductrevReviewTabCounterList = ProductrevReviewTabCounterList()

)