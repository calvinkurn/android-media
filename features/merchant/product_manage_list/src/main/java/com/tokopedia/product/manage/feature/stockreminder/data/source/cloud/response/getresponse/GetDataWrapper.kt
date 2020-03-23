package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse

import com.google.gson.annotations.SerializedName

data class GetDataWrapper(
        @SerializedName("data")
        val data: List<GetProduct>
)