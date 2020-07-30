package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse

import com.google.gson.annotations.SerializedName

data class CreateStockReminderResponse(
        @SerializedName("IMSCreateStockAlertThreshold")
        var createStockAlertThreshold: CreateUpdateDataWrapper
)