package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse

import com.google.gson.annotations.SerializedName

data class DataWrapper(
        @SerializedName("data")
        val data: List<CreateUpdateProduct>
)