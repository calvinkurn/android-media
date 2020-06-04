package com.tokopedia.tkpd.tkpdreputation.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRevIncentiveOvo(
        @SerializedName("productrevIncentiveOvo")
        @Expose
        val productrevIncentiveOvo: ProductRevIncentiveOvoResponse? = ProductRevIncentiveOvoResponse()
)