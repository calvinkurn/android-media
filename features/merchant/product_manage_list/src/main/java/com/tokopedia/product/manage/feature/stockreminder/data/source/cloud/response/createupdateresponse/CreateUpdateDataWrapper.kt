package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse

import com.google.gson.annotations.SerializedName

data class CreateUpdateDataWrapper(
        @SerializedName("data")
        val data: List<CreateUpdateProduct>
)